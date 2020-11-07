package apsh.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import apsh.backend.po.OrderProduction;

@Repository
public interface OrderProductionRepository extends JpaRepository<OrderProduction, Integer> {



}