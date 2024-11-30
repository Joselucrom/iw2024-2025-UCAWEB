package uca.es.iw.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername(String username);

    //List<User> findByUsernameContainingAndEmailContainingAndRoles_NameContaining(String username, String email, Role role);

    //List<User> findByUsernameContainingAndEmailContaining(String username, String email);
}
