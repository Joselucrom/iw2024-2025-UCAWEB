package uca.es.iw.views.perfil;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.RolesAllowed;
import uca.es.iw.security.AuthenticatedUser;
import uca.es.iw.data.User;
import uca.es.iw.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Route(value = "perfil", layout = uca.es.iw.views.MainLayout.class)
@PageTitle("Perfil de Usuario")
@RolesAllowed("USER")
public class PerfilView extends VerticalLayout {

    private final AuthenticatedUser authenticatedUser;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    // Campos del formulario
    private final TextField username = new TextField("Nombre de usuario");
    private final TextField fullName = new TextField("Nombre completo");
    private final TextField roles = new TextField("Rol de usuario");
    private final PasswordField password = new PasswordField("Nueva contraseña");
    private final Image profilePicture = new Image();
    private final Upload imageUpload;
    private byte[] uploadedImage;

    public PerfilView(AuthenticatedUser authenticatedUser, UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticatedUser = authenticatedUser;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            // Configurar campos
            username.setValue(user.getUsername());
            fullName.setValue(user.getName());
            roles.setValue(user.getRoles().toString());
            roles.setReadOnly(true);
            // Configurar imagen de perfil
            if (user.getProfilePicture() != null && user.getProfilePicture().length > 0) {
                profilePicture.setSrc(new StreamResource("profile-pic",
                        () -> new ByteArrayInputStream(user.getProfilePicture())));
            } else {
                profilePicture.setSrc("https://via.placeholder.com/150");
            }
            profilePicture.setWidth("150px");
            profilePicture.setHeight("150px");
        }
        // Configurar la carga de imagen con MemoryBuffer
        MemoryBuffer buffer = new MemoryBuffer();
        imageUpload = new Upload(buffer);
        imageUpload.setAcceptedFileTypes("image/jpeg", "image/png");
        imageUpload.addSucceededListener(event -> {
            try (InputStream inputStream = buffer.getInputStream()) {
                uploadedImage = inputStream.readAllBytes();
                profilePicture.setSrc(new StreamResource("profile-pic",
                        () -> new ByteArrayInputStream(uploadedImage)));
                Notification.show("Imagen cargada correctamente.");
            } catch (IOException e) {
                Notification.show("Error al cargar la imagen: " + e.getMessage());
            }
        });
        password.getElement().setAttribute("autocomplete", "new-password");
        Button saveButton = new Button("Guardar cambios", event -> {
            // Validar campos antes de guardar
            if (!validarCampos()) {
                return; // Si la validación falla, se detiene la ejecución.
            }
            maybeUser.ifPresent(user -> {
                // Actualizar nombre completo solo si no está vacío
                String newFullName = fullName.getValue();
                if (!newFullName.isEmpty()) {
                    user.setName(newFullName);
                }
                // Actualizar nombre de usuario solo si no está vacío y es diferente
                String newUsername = username.getValue();
                if (!newUsername.isEmpty() && !newUsername.equals(user.getUsername())) {
                    user.setUsername(newUsername);
                }
                // Solo actualizamos la contraseña si se ha modificado
                if (!password.getValue().isEmpty()) {
                    String hashedPassword = encriptarContraseña(password.getValue());
                    user.setHashedPassword(hashedPassword);
                }
                // Actualizar imagen si se ha subido
                if (uploadedImage != null) {
                    user.setProfilePicture(uploadedImage);
                }
                // Guardar cambios en la base de datos
                userService.updateUserData(user);
                Notification.show("Perfil actualizado correctamente.");
                // Actualizar el contexto de autenticación para reflejar el cambio de nombre de usuario
                authenticatedUser.setUsername(user);
                // Actualizar la página para reflejar los cambios
                getUI().ifPresent(ui -> ui.getPage().reload());
            });
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        // Diseño
        setWidth("100%");
        setAlignItems(Alignment.CENTER);

        username.setWidth("100%");
        fullName.setWidth("100%");
        password.setWidth("100%");
        roles.setWidth("100%");
        // Agregar componentes al layout
        add(username, fullName, roles, password, profilePicture, imageUpload, saveButton);
    }
    public boolean validarCampos() {
        if (username == null || username.getValue().isEmpty()) {
            Notification.show("El campo 'Nombre de usuario' no puede estar vacío.");
            return false;
        }
        if (fullName == null || fullName.getValue().isEmpty()) {
            Notification.show("El campo 'Nombre completo' no puede estar vacío.");
            return false;
        }
        return true;
    }
    private String encriptarContraseña(String password) {
        return passwordEncoder.encode(password);
    }
}