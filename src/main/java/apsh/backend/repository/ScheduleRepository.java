package apsh.backend.repository;

import apsh.backend.po.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Order, Integer> {
}
