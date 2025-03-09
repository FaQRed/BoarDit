package pl.sankouski.boarditdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sankouski.boarditdata.model.user.Role;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
