package pl.palkaszymon.ecommercewebflux.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.palkaszymon.ecommercewebflux.database.entity.ProductEntity;
import pl.palkaszymon.ecommercewebflux.domain.request.NewProductRequest;
import pl.palkaszymon.ecommercewebflux.domain.request.UpdateProductRequest;
import pl.palkaszymon.ecommercewebflux.domain.service.ProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public Flux<ProductEntity> getAllProducts() {
        log.info("Get all products request");
        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    public Mono<ProductEntity> getProductById(@PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    @PostMapping
    public Mono<ProductEntity> saveProduct(@RequestBody NewProductRequest request) {
        log.info("Saving product: {}", request.name());
        return productService.createProduct(request);
    }

    @PatchMapping("/{productId}")
    public Mono<ProductEntity> updateProduct(@PathVariable Long productId, @RequestBody UpdateProductRequest request) {
        return productService.updateProduct(productId, request);
    }

    @DeleteMapping
    public Mono<Void> deleteProduct() {
        log.info("Deleted all products");
        return productService.deleteAllProducts();
    }
}
