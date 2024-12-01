package uca.es.iw.views.moduser;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import uca.es.iw.services.UserService;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RolesAllowed("ADMIN")
@PageTitle("Editar usuario")
@Route(value = "modify-user/:userID")
public class ModifyUserView extends Composite<VerticalLayout> implements BeforeEnterObserver {

    private final UserService userService;

    private Long userId; // Para almacenar el ID del usuario a modificar
    private Avatar avatar;
    private byte[] profilePictureData; // Almacena temporalmente la imagen cargada

    private TextField nameField;
    private TextField usernameField;
    private PasswordField passwordField;
    private ComboBox<String> roleComboBox;
    private EmailField emailField;

    @Autowired
    public ModifyUserView(UserService userService) {
        this.userService = userService;

        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3("Modificar información del usuario");

        // Formulario
        FormLayout formLayout = new FormLayout();
        nameField = new TextField("Nombre");
        usernameField = new TextField("Nombre de usuario");
        passwordField = new PasswordField("Nueva contraseña (opcional)");
        roleComboBox = new ComboBox<>("Tipo de usuario");
        emailField = new EmailField("Email");

        roleComboBox.setItems("USER", "ADMIN", "OTP", "CIO", "PROMOTOR");
        roleComboBox.setPlaceholder("Seleccione un rol");

        // Avatar
        avatar = new Avatar();
        avatar.setName("Vista previa");

        // Componente de subida de imágenes
        Upload upload = getUpload();

        // Botón para guardar
        Button saveButton = new Button("Guardar");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> saveUser());

        // Diseño principal
        formLayout.add(nameField, usernameField, passwordField, roleComboBox, emailField);
        layoutColumn2.add(h3, formLayout, avatar, upload, saveButton);

        getContent().add(layoutColumn2);
        getContent().setAlignItems(Alignment.CENTER);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Obtén el ID del usuario desde la ruta
        String userIdStr = event.getRouteParameters().get("userID").orElse(null);
        if (userIdStr != null) {
            try {
                userId = Long.parseLong(userIdStr);
                // Carga los datos del usuario
                userService.get(userId).ifPresentOrElse(
                        this::populateForm,
                        () -> {
                            Notification.show("Usuario no encontrado.", 3000, Notification.Position.MIDDLE);
                            event.forwardTo("search-users"); // Redirige si el usuario no existe
                        }
                );
            } catch (NumberFormatException e) {
                Notification.show("ID de usuario inválido.", 3000, Notification.Position.MIDDLE);
                event.forwardTo("search-users");
            }
        }
    }

    private Upload getUpload() {
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("image/jpeg", "image/png");
        upload.setMaxFileSize(2 * 1024 * 1024);

        upload.addSucceededListener(event -> {
            try {
                profilePictureData = buffer.getInputStream().readAllBytes();
                avatar.setImageResource(new StreamResource(event.getFileName(),
                        () -> new ByteArrayInputStream(profilePictureData)));
                Notification.show("Imagen cargada correctamente", 3000, Notification.Position.MIDDLE);
            } catch (IOException e) {
                Notification.show("Error al cargar la imagen", 3000, Notification.Position.MIDDLE);
            }
        });

        upload.addFileRemovedListener(event -> {
            avatar.setImageResource(null);
            profilePictureData = null;
        });

        return upload;
    }

    private void clearForm() {
        nameField.clear();
        usernameField.clear();
        passwordField.clear();
        emailField.clear();
        avatar.setImageResource(null);
        profilePictureData = null;
    }

    private void populateForm(uca.es.iw.data.User user) {
        nameField.setValue(user.getName() != null ? user.getName() : "");
        usernameField.setValue(user.getUsername() != null ? user.getUsername() : "");
        emailField.setValue(user.getEmail() != null ? user.getEmail() : "");

        if (user.getProfilePicture() != null) {
            profilePictureData = user.getProfilePicture();
            avatar.setImageResource(new StreamResource("profilePicture",
                    () -> new ByteArrayInputStream(profilePictureData)));
        }
    }

    private void saveUser() {
        if (nameField.getValue().isEmpty() || usernameField.getValue().isEmpty() || emailField.getValue().isEmpty()) {
            Notification.show("Por favor, complete todos los campos obligatorios.", 3000, Notification.Position.MIDDLE);
            return;
        }

        try {
            userService.updateUser(
                    userId,
                    nameField.getValue(),
                    usernameField.getValue(),
                    passwordField.getValue().isEmpty() ? null : passwordField.getValue(),
                    emailField.getValue(),
                    profilePictureData,
                    roleComboBox.getValue()
            );
            Notification.show("Usuario actualizado con éxito.", 3000, Notification.Position.MIDDLE);
        } catch (IllegalArgumentException e) {
            Notification.show(e.getMessage(), 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("Error al actualizar el usuario: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
        //volver a la vista de búsqueda de usuarios
        UI.getCurrent().navigate("search-users");
    }
}
