package uca.es.iw.views.perfil;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import uca.es.iw.security.AuthenticatedUser;
import uca.es.iw.data.User;
import uca.es.iw.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Route(value = "perfil", layout = uca.es.iw.views.MainLayout.class)
@RolesAllowed("USER")
public class PerfilView extends VerticalLayout {

    private final AuthenticatedUser authenticatedUser;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final I18NProvider i18nProvider;

    // Campos del formulario
    private final TextField username = new TextField();
    private final TextField fullName = new TextField();
    private final TextField email = new TextField();
    private final TextField roles = new TextField();
    private final PasswordField password = new PasswordField();
    private final Image profilePicture = new Image();
    private final Upload imageUpload;
    private byte[] uploadedImage;
    @Autowired
    public PerfilView(AuthenticatedUser authenticatedUser, UserService userService, PasswordEncoder passwordEncoder, I18NProvider i18nProvider) {
        this.authenticatedUser = authenticatedUser;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.i18nProvider = i18nProvider;
        // Establecer el título de la página, que luego se pasará al MainLayout
        getUI().ifPresent(ui -> ui.getPage().setTitle(i18nProvider.getTranslation("perfil.titulo", getLocale())));
        // Configurar campos iniciales
        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            username.setValue(user.getUsername());
            fullName.setValue(user.getName());
            roles.setValue(user.getRoles().toString());
            roles.setReadOnly(true);
            email.setValue(user.getEmail());
            if (user.getProfilePicture() != null && user.getProfilePicture().length > 0) {
                profilePicture.setSrc(new StreamResource("profile-pic",
                        () -> new ByteArrayInputStream(user.getProfilePicture())));
            } else {
                profilePicture.setSrc("https://via.placeholder.com/150");
            }
            profilePicture.setWidth("150px");
            profilePicture.setHeight("150px");
        }
        // Configurar la carga de imágenes
        MemoryBuffer buffer = new MemoryBuffer();
        imageUpload = new Upload(buffer);
        imageUpload.setAcceptedFileTypes("image/jpeg", "image/png");
        imageUpload.addSucceededListener(event -> {
            try (InputStream inputStream = buffer.getInputStream()) {
                uploadedImage = inputStream.readAllBytes();
                profilePicture.setSrc(new StreamResource("profile-pic",
                        () -> new ByteArrayInputStream(uploadedImage)));
                Notification.show(i18nProvider.getTranslation("perfil.imagen_cargada", getLocale()));
            } catch (IOException e) {
                Notification.show(i18nProvider.getTranslation("perfil.error_imagen", getLocale(), e.getMessage()));
            }
        });
        password.getElement().setAttribute("autocomplete", "new-password");
        // Botón para guardar cambios
        Button saveButton = new Button(i18nProvider.getTranslation("perfil.guardar_cambios", getLocale()), event -> {
            if (!validarCampos(i18nProvider)) {
                return;
            }
            maybeUser.ifPresent(user -> {
                // Actualizar los datos del usuario
                user.setName(fullName.getValue());
                user.setUsername(username.getValue());
                if (!password.getValue().isEmpty()) {
                    user.setHashedPassword(encriptarContraseña(password.getValue()));
                }
                if (uploadedImage != null) {
                    user.setProfilePicture(uploadedImage);
                }
                userService.updateUserData(user);

                // Reautenticar al usuario con los datos actualizados
                authenticatedUser.reauthenticate(user);

                Notification.show(i18nProvider.getTranslation("perfil.perfil_actualizado", getLocale()));
                getUI().ifPresent(ui -> ui.getPage().reload());
            });
        });
        // Botón para eliminar datos personales
        Button deletePersonalDataButton = new Button(
                i18nProvider.getTranslation("perfil.eliminar_datos", getLocale()),
                event -> showConfirmationDialog(
                        i18nProvider.getTranslation("perfil.eliminar_datos_confirmacion", getLocale()),
                        () -> maybeUser.ifPresent(user -> {
                            userService.deletePersonalData(user);
                            Notification.show(i18nProvider.getTranslation("perfil.datos_eliminados", getLocale()));
                            getUI().ifPresent(ui -> ui.getPage().reload());
                        })
                )
        );
        deletePersonalDataButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        // Etiquetas de los campos con traducción
        username.setLabel(i18nProvider.getTranslation("perfil.nombre_usuario", getLocale()));
        fullName.setLabel(i18nProvider.getTranslation("perfil.nombre_completo", getLocale()));
        roles.setLabel(i18nProvider.getTranslation("perfil.rol_usuario", getLocale()));
        email.setLabel(i18nProvider.getTranslation("perfil.correo_electronico", getLocale()));
        password.setLabel(i18nProvider.getTranslation("perfil.nueva_contrasena", getLocale()));
        // Diseño
        setWidth("100%");
        setAlignItems(Alignment.CENTER);
        username.setWidth("100%");
        fullName.setWidth("100%");
        password.setWidth("100%");
        roles.setWidth("100%");
        email.setWidth("100%");
        add(username, fullName, email, password, roles, profilePicture, imageUpload, saveButton, deletePersonalDataButton);
    }
    private boolean validarCampos(I18NProvider i18nProvider) {
        if (username.getValue().isEmpty() || fullName.getValue().isEmpty()) {
            Notification.show(i18nProvider.getTranslation("perfil.campos_invalidos", getLocale()));
            return false;
        }
        return true;
    }
    private String encriptarContraseña(String password) {
        return passwordEncoder.encode(password);
    }
    private void showConfirmationDialog(String message, Runnable onConfirm) {
        Dialog confirmationDialog = new Dialog();
        confirmationDialog.add(new H1(message));
        Button confirmButton = new Button(i18nProvider.getTranslation("perfil.confirmar", getLocale()), event -> {
            onConfirm.run();
            confirmationDialog.close();
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelButton = new Button(i18nProvider.getTranslation("perfil.cancelar", getLocale()), event -> confirmationDialog.close());
        confirmationDialog.add(confirmButton, cancelButton);
        confirmationDialog.open();
    }
}