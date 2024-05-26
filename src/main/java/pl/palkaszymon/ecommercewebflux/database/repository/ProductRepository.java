package pl.palkaszymon.ecommercewebflux.database.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pl.palkaszymon.ecommercewebflux.database.entity.ProductEntity;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, Long> {
}
