package pl.palkaszymon.ecommercewebflux.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.palkaszymon.ecommercewebflux.domain.exception.ValidationException;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum Category {
    MOBILE_PHONE("Mobile Phone"),
    COMPUTER_PART("Computer Part"),
    LAPTOP("Laptop"),
    ACCESSORY("Accessory"),
    MISCELLANEOUS("Miscellaneous");

    private final String name;

    public static Category fromString(String categoryName) {
        return Stream.of(Category.values())
                .filter(category -> category.name.equals(categoryName))
                .findFirst()
                .orElseThrow(() -> new ValidationException("No such category available!"));
    }
}
