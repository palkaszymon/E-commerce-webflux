package pl.palkaszymon.ecommercewebflux.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.palkaszymon.ecommercewebflux.messaging.messages.CustomerNotificationServiceMessage;
import pl.palkaszymon.ecommercewebflux.messaging.messages.NewOrderMessage;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Sender;

@RequiredArgsConstructor
@Service
public class RabbitMQService {

    private final String WAREHOUSE_NOTIFICATION_QUEUE = "q.warehouse-notifications";
    private final String CUSTOMER_NOTIFICATION_SERVICE_QUEUE = "q.customer-service-notifications";

    private final Sender sender;
    private final ObjectMapper objectMapper;

    public Mono<Void> sendOrderNotificationToWarehouse(NewOrderMessage message) {
        return sendNotification("", WAREHOUSE_NOTIFICATION_QUEUE, message);
    }

    public Mono<Void> sendOrderNotificationToCustomerNotificationService(CustomerNotificationServiceMessage message) {
        return sendNotification("", CUSTOMER_NOTIFICATION_SERVICE_QUEUE, message);
    }

    private <T> Mono<Void> sendNotification(String exchange, String routingKey, T message) {
        return Mono.fromCallable(() -> serialize(message))
                .flatMap(serializedMessage -> sender.send(Mono.just(new OutboundMessage(exchange, routingKey, serializedMessage))))
                .then();
    }

    private byte[] serialize(Object object) {
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize message", e);
        }
    }


}
