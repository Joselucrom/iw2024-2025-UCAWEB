package uca.es.iw.security;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uca.es.iw.data.User;
import uca.es.iw.data.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

@Component
public class AuthenticatedUser {

    private final UserRepository userRepository;
    private final AuthenticationContext authenticationContext;

    public AuthenticatedUser(AuthenticationContext authenticationContext, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.authenticationContext = authenticationContext;
    }

    @Transactional
    public Optional<User> get() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class)
                .map(userDetails -> userRepository.findByUsername(userDetails.getUsername()).get());
    }

    public void logout() {
        authenticationContext.logout();
    }

    public void reauthenticate(User user) {
        // Crear un nuevo UserDetails actualizado
        UserDetails updatedUserDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getHashedPassword())
                .authorities(user.getRoles().stream()
                        .map(role -> "ROLE_" + role.name()) // Asegura prefijo "ROLE_"
                        .toArray(String[]::new))
                .build();

        // Actualizar el contexto de seguridad con el nuevo UserDetails
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(updatedUserDetails, null, updatedUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}