package apsh.backend.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "delivery_date")
    private Date deliveryDate;

    @Column(name = "product_count")
    private Integer productCount;

    @Column(name = "is_deleted")
    private Integer isDeleted;

}
