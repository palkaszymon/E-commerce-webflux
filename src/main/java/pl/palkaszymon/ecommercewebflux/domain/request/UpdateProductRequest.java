package pl.palkaszymon.ecommercewebflux.domain.request;

public record UpdateProductRequest(String name, String description, String category, Double price) {
}
