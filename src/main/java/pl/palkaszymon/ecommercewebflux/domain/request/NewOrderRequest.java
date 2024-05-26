package pl.palkaszymon.ecommercewebflux.domain.request;

import java.util.Set;

public record NewOrderRequest(String email, String address, String shippingMethod, Set<Long> productIds) {
}
