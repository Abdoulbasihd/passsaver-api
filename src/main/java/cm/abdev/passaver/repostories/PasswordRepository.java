package cm.abdev.passaver.repostories;

import cm.abdev.passaver.models.Password;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordRepository extends JpaRepository<Password, Long> {

    List<Password> findByAppName(String appName);


}
