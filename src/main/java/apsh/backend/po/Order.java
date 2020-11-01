package apsh.backend.po;

import apsh.backend.dto.CustomerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "orders")
public class Order {

    public static final int NOT_DELETED = 0;
    public static final int DELETED = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "delivery_date")
    private Date deliveryDate;

    @Column(name = "product_count")
    private Integer productCount;

    @Column(name = "is_deleted")
    private Integer isDeleted;

    public Order(CustomerOrderDto order) {
        this.productId = order.getProductId();
        this.deliveryDate = new Date(order.getDayOfDelivery().getTime());
        this.productCount = order.getProductCount();
    }
}
