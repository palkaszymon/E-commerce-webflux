package pl.palkaszymon.ecommercewebflux.messaging.messages;

import java.io.Serializable;

public record NewOrderMessage(Long orderId) implements Serializable {
}
