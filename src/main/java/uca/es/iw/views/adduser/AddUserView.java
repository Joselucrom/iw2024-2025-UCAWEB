package uca.es.iw.views.adduser;

import com.vaadin.flow.component.Composite;
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
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import uca.es.iw.services.UserService;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@PageTitle("Añadir usuario")
@Route(value = "add-user")
@Menu(order = 3, icon = "line-awesome/svg/user.svg")
@RolesAllowed("ADMIN")
public class AddUserView extends Composite<VerticalLayout> {

    private Avatar avatar; // Muestra la imagen de perfil cargada
    private byte[] profilePictureData; // Almacena temporalmente la imagen cargada

    @Autowired
    private UserService userService;  // Inyección de dependencia de UserService

    private TextField nameField;
    private TextField usernameField;
    private PasswordField passwordField;
    private ComboBox<String> roleComboBox;
    private EmailField emailField;

    public AddUserView() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3("Información del usuario");

        // Formulario
        FormLayout formLayout = new FormLayout();
        nameField = new TextField("Nombre");
        usernameField = new TextField("Nombre de usuario");
        passwordField = new PasswordField("Contraseña");
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
        saveButton.addClickListener(event -> saveUser(
                nameField.getValue(),
                usernameField.getValue(),
                passwordField.getValue(),
                roleComboBox.getValue(),
                emailField.getValue()
        ));

        // Diseño principal
        formLayout.add(nameField, usernameField, passwordField, roleComboBox, emailField);
        layoutColumn2.add(h3, formLayout, avatar, upload, saveButton);

        getContent().add(layoutColumn2);
        getContent().setAlignItems(Alignment.CENTER);
    }

    private Upload getUpload() {
        MemoryBuffer buffer = new MemoryBuffer(); // Buffer para almacenar la imagen
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("image/jpeg", "image/png"); // Tipos de archivo aceptados
        upload.setMaxFileSize(2 * 1024 * 1024); // Tamaño máximo: 2MB

        // Configuración de eventos para subir la imagen
        upload.addSucceededListener(event -> {
            InputStream inputStream = buffer.getInputStream();
            try {
                profilePictureData = inputStream.readAllBytes(); // Lee los datos de la imagen
                avatar.setImageResource(new StreamResource(event.getFileName(),
                        () -> new ByteArrayInputStream(profilePictureData))); // Muestra la imagen
                Notification.show("Imagen cargada correctamente", 3000, Notification.Position.MIDDLE);
            } catch (IOException e) {
                Notification.show("Error al cargar la imagen", 3000, Notification.Position.MIDDLE);
            }
        });

        // Evento para cuando el archivo es eliminado
        upload.addFileRemovedListener(event -> {
            avatar.setImageResource(null); // Elimina la imagen de vista previa
            profilePictureData = null; // Limpia la imagen almacenada temporalmente
        });

        return upload;
    }

    private void clearForm() {
        nameField.clear();
        usernameField.clear();
        passwordField.clear();
        roleComboBox.clear();
        emailField.clear();
        avatar.setImageResource(null); // Elimina la imagen de vista previa
        profilePictureData = null;    // Limpia la imagen almacenada temporalmente
    }

    private void saveUser(String name, String username, String password, String role, String email) {
        try {
            userService.createUser(name, username, password, email, profilePictureData, role);
            Notification.show("Usuario guardado con éxito.", 3000, Notification.Position.MIDDLE);

            // Limpiar campos después de guardar
            clearForm();

        } catch (IllegalArgumentException e) {
            Notification.show(e.getMessage(), 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            System.err.println("Detalles del error: " + e.getCause());
            e.printStackTrace();
            Notification.show("Ocurrió un error al guardar el usuario." + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
}
