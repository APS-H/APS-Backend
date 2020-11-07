package apsh.backend.repository;

import apsh.backend.po.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

     Optional<Order> findById(Integer id);
}
