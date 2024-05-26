package pl.palkaszymon.ecommercewebflux.domain.validator;

import org.springframework.stereotype.Component;
import pl.palkaszymon.ecommercewebflux.domain.exception.ValidationException;
import pl.palkaszymon.ecommercewebflux.domain.request.NewProductRequest;
import pl.palkaszymon.ecommercewebflux.domain.request.UpdateProductRequest;

@Component
public class ProductRequestValidator {
    public void validateNewProductRequest(NewProductRequest request) {
        checkNotNullOrEmpty(request.name(), "Name cannot be null or empty!");
        checkNotNullOrEmpty(request.category(), "Category cannot be null or empty!");
        checkNotNull(request.price(), "Price cannot be null!");
    }

    public void validateUpdateProductRequest(UpdateProductRequest request) {
        if (request.name() != null) {
            checkNotNullOrEmpty(request.name(), "Name cannot be empty!");
        }
        if (request.category() != null) {
            checkNotNullOrEmpty(request.category(), "Category cannot be empty!");
        }
    }

    private void checkNotNullOrEmpty(String param, String exceptionMessage) {
        if (param == null || param.isBlank() || param.isEmpty()) {
            throw new ValidationException(exceptionMessage);
        }
    }

    private <T> void checkNotNull(T param, String exceptionMessage) {
        if (param == null) {
            throw new ValidationException(exceptionMessage);
        }
    }
}