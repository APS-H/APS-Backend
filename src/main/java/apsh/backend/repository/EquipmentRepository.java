package apsh.backend.repository;

import apsh.backend.po.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {

    Optional<Equipment> findByName(String name);

}
