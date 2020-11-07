package apsh.backend.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "order_production")
public class OrderProduction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "order_id")
    private String orderId;

    @JoinColumn(name = "order_production_id")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SuborderProduction> suborderProductions;
<<<<<<< HEAD

    public List<SuborderProduction> getSuborderProductionsByDate(Date date) {
        List<SuborderProduction> origin = new ArrayList<>(suborderProductions);


        return origin.stream().filter(s -> {
            Date date1 = Date.from(s.getStartTime());
            Date date2 = Date.from(s.getEndTime());


            return DateUtils.isSameDay(date, date1) && DateUtils.isSameDay(date, date2);
        }).collect(Collectors.toList());

    }
}
=======
}
>>>>>>> dd69359db8668e9a9802ab8527197a421980fde3
