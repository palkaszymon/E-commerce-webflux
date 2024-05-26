package pl.palkaszymon.ecommercewebflux.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import pl.palkaszymon.ecommercewebflux.domain.model.Category;

@Table("products")
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductEntity {
    @Id
    private Long id;
    private String name;
    private String description;
    private Category category;
    private Double price;
}
