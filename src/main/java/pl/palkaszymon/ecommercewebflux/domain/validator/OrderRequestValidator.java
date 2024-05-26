package pl.palkaszymon.ecommercewebflux.domain.validator;

import org.springframework.stereotype.Component;
import pl.palkaszymon.ecommercewebflux.domain.exception.ValidationException;
import pl.palkaszymon.ecommercewebflux.domain.request.NewOrderRequest;

import java.util.Set;
import java.util.regex.Pattern;

@Component
public class OrderRequestValidator {
    public void validate(NewOrderRequest request) {
        validateEmail(request.email());
        checkNotNullOrEmpty(request.address(), "Address cannot be empty!");
        checkNotNullOrEmpty(request.shippingMethod(), "Shipping method cannot be empty!");
        validateProductIds(request.productIds());
    }

    private void validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.matches(emailRegex, email)) {
            throw new ValidationException("Invalid email format");
        }
    }

    private void validateProductIds(Set<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            throw new ValidationException("Product IDs cannot be empty.");
        }
        if (productIds.contains(null)) {
            throw new ValidationException("Product IDs cannot contain null.");
        }
    }


    private void checkNotNullOrEmpty(String param, String exceptionMessage) {
        if (param == null || param.isBlank() || param.isEmpty()) {
            throw new ValidationException(exceptionMessage);
        }
    }
}