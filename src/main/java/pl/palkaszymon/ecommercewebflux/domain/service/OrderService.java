package pl.palkaszymon.ecommercewebflux.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.palkaszymon.ecommercewebflux.database.entity.OrderEntity;
import pl.palkaszymon.ecommercewebflux.database.repository.OrderRepository;
import pl.palkaszymon.ecommercewebflux.database.repository.ProductRepository;
import pl.palkaszymon.ecommercewebflux.domain.exception.OrderNotFoundException;
import pl.palkaszymon.ecommercewebflux.domain.exception.ProductNotFoundException;
import pl.palkaszymon.ecommercewebflux.domain.model.OrderStatus;
import pl.palkaszymon.ecommercewebflux.domain.model.ShippingMethod;
import pl.palkaszymon.ecommercewebflux.domain.request.NewOrderRequest;
import pl.palkaszymon.ecommercewebflux.domain.validator.OrderRequestValidator;
import pl.palkaszymon.ecommercewebflux.messaging.RabbitMQService;
import pl.palkaszymon.ecommercewebflux.messaging.messages.CustomerNotificationServiceMessage;
import pl.palkaszymon.ecommercewebflux.messaging.messages.NewOrderMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final RabbitMQService rabbitMQService;
    private final OrderRequestValidator orderRequestValidator;

    @Transactional
    public Mono<OrderEntity> createOrder(NewOrderRequest request) {
        return Mono.just(request)
                .doOnNext(orderRequestValidator::validate)
                .flatMap(req -> checkRequestProductsExistence(req.productIds())
                        .then(Mono.just(new OrderEntity(null, req.email(), req.address(), LocalDateTime.now(),
                                ShippingMethod.fromString(req.shippingMethod()), OrderStatus.PROCESSING, req.productIds()))))
                .flatMap(orderRepository::save)
                .flatMap(order -> sendOrderNotifications(order)
                        .thenReturn(order));
    }

    public Mono<OrderEntity> updateOrderStatus(Long orderId) {
        return orderRepository.findById(orderId)
                .flatMap(order -> {
                    order.setOrderStatus(OrderStatus.CONFIRMED);
                    return orderRepository.save(order);
                })
                .switchIfEmpty(Mono.error(new OrderNotFoundException(String.format("Order with id: %s doesn't exist!", orderId))));
    }

    private Mono<Void> checkRequestProductsExistence(Set<Long> productIds) {
        return Flux.fromIterable(productIds)
                .flatMap(productId -> productRepository.findById(productId)
                        .switchIfEmpty(Mono.error(new ProductNotFoundException(String.format("Product with ID: %s not found", productId)))))
                .then();
    }

    private Mono<Void> sendOrderNotifications(OrderEntity savedOrder) {
        NewOrderMessage newOrderMessage = new NewOrderMessage(savedOrder.getId());
        CustomerNotificationServiceMessage customerMessage = new CustomerNotificationServiceMessage(
                savedOrder.getId(), savedOrder.getEmail(), savedOrder.getShippingMethod().getName());

        return rabbitMQService.sendOrderNotificationToWarehouse(newOrderMessage)
                .then(rabbitMQService.sendOrderNotificationToCustomerNotificationService(customerMessage));
    }
}
