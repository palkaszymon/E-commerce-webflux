package pl.palkaszymon.ecommercewebflux.messaging.messages;

import java.io.Serializable;

public record OrderConfirmedMessage(Long orderId, String orderStatus) implements Serializable {
}
