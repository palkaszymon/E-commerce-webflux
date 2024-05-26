package pl.palkaszymon.ecommercewebflux.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.palkaszymon.ecommercewebflux.domain.exception.ValidationException;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum ShippingMethod {
    POST("Post"),
    COURIER("Courier"),
    PACKSTATION("Packstation");

    private final String name;

    public static ShippingMethod fromString(String categoryName) {
        return Stream.of(ShippingMethod.values())
                .filter(category -> category.name.equals(categoryName))
                .findFirst()
                .orElseThrow(() -> new ValidationException("No such shipping method available!"));
    }
}
