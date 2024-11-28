package uca.es.iw.services;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import uca.es.iw.data.User;
import uca.es.iw.data.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Set;
import uca.es.iw.data.Role;


@Service
public class UserService {



    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    //public UserService(UserRepository repository) {
    //    this.repository = repository;
    //}

    public Optional<User> get(Long id) {
        return repository.findById(id);
    }

    public User update(User entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<User> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<User> list(Pageable pageable, Specification<User> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }


    public User createUser(String name, String username, String rawPassword, String email, byte[] profilePicture, String role) {
        if (repository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }


        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setHashedPassword(passwordEncoder.encode(rawPassword)); // Hashear la contraseña
        user.setEmail(email);
        user.setProfilePicture(profilePicture);
        user.setRoles(Set.of(Role.valueOf(role)));

        return repository.save(user);
    }
}
