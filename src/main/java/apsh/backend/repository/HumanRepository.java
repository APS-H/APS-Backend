package apsh.backend.repository;

import apsh.backend.po.Human;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HumanRepository extends JpaRepository<Human, Integer> {

    Optional<Human> findByGroupName(String groupName);

}
