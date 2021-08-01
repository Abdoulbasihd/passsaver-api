package cm.abdev.passaver.repostories.auth;

import cm.abdev.passaver.models.auth.ERole;
import cm.abdev.passaver.models.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository <Role, Long> {

    Optional<Role>  findByName(ERole roleName);


}
