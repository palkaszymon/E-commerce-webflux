package pl.palkaszymon.ecommercewebflux.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.palkaszymon.ecommercewebflux.domain.service.OrderService;
import pl.palkaszymon.ecommercewebflux.messaging.messages.OrderConfirmedMessage;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.Receiver;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderConfirmationListener {

    private final Receiver receiver;
    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    public void setupListener() {
        receiver.consumeAutoAck("q.order-confirmations")
                .flatMap(delivery -> deserialize(delivery.getBody()))
                .filter(message -> "CONFIRMED".equals(message.orderStatus()))
                .flatMap(message -> orderService.updateOrderStatus(message.orderId()))
                .subscribe(message -> log.info("Processed order confirmation: {}", message.getId()),
                        error -> log.error("Error processing message", error));
    }

    private Mono<OrderConfirmedMessage> deserialize(byte[] body) {
        return Mono.fromCallable(() -> objectMapper.readValue(body, OrderConfirmedMessage.class))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
