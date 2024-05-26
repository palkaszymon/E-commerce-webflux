package pl.palkaszymon.ecommercewebflux.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import pl.palkaszymon.ecommercewebflux.domain.model.OrderStatus;
import pl.palkaszymon.ecommercewebflux.domain.model.ShippingMethod;

import java.time.LocalDateTime;
import java.util.Set;

@Table("orders")
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderEntity {
    @Id
    private Long id;
    private String email;
    private String address;
    private LocalDateTime orderDate;
    private ShippingMethod shippingMethod;
    private OrderStatus orderStatus;
    private Set<Long> productIds;
}
