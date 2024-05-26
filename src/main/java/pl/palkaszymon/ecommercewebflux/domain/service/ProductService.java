package pl.palkaszymon.ecommercewebflux.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.palkaszymon.ecommercewebflux.database.entity.ProductEntity;
import pl.palkaszymon.ecommercewebflux.database.repository.ProductRepository;
import pl.palkaszymon.ecommercewebflux.domain.exception.ProductNotFoundException;
import pl.palkaszymon.ecommercewebflux.domain.model.Category;
import pl.palkaszymon.ecommercewebflux.domain.request.NewProductRequest;
import pl.palkaszymon.ecommercewebflux.domain.request.UpdateProductRequest;
import pl.palkaszymon.ecommercewebflux.domain.validator.ProductRequestValidator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductRequestValidator productRequestValidator;

    public Mono<ProductEntity> createProduct(NewProductRequest request) {
        productRequestValidator.validateNewProductRequest(request);
        ProductEntity productToSave = new ProductEntity(null, request.name(), request.description(), Category.fromString(request.category()), request.price());
        return productRepository.save(productToSave);
    }

    public Mono<ProductEntity> updateProduct(Long productId, UpdateProductRequest request) {
        productRequestValidator.validateUpdateProductRequest(request);
        return getProductById(productId)
                .flatMap(productToUpdate -> productRepository.save(prepareEntityToUpdate(productToUpdate, request)));
    }

    public Mono<ProductEntity> getProductById(Long productId) {
        return productRepository.findById(productId)
                .switchIfEmpty(Mono.error(new ProductNotFoundException("Product with ID: " + productId + " not found")));
    }

    public Flux<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    private ProductEntity prepareEntityToUpdate(ProductEntity foundEntity, UpdateProductRequest updateRequest) {
        if (updateRequest.name() != null) {
            foundEntity.setName(updateRequest.name());
        }
        if (updateRequest.description() != null) {
            foundEntity.setDescription(updateRequest.description());
        }
        if (updateRequest.category() != null) {
            foundEntity.setCategory(Category.fromString(updateRequest.category()));
        }
        if (updateRequest.price() != null) {
            foundEntity.setPrice(updateRequest.price());
        }
        return foundEntity;
    }

    public Mono<Void> deleteAllProducts() {
        return productRepository.deleteAll();
    }
}
