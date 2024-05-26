package pl.palkaszymon.ecommercewebflux.domain.request;

public record NewProductRequest(String name, String description, String category, Double price) {
}
