package apsh.backend.repository;

import apsh.backend.po.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Integer> {

    Optional<Shift> findByName(String name);

}
