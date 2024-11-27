package uca.es.iw.views.adduser;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import elemental.json.JsonObject;
import jakarta.annotation.security.RolesAllowed;
//import org.vaadin.lineawesome.LineAwesomeIconUrl;
import com.vaadin.flow.server.StreamResource;
import java.io.ByteArrayInputStream;


@PageTitle("Añadir usuario")
@Route(value = "add-user")
@Menu(order = 3, icon = "line-awesome/svg/user.svg")
@RolesAllowed("ADMIN")
public class AddUserView extends Composite<VerticalLayout> {

    private Avatar avatar;
    private byte[] profilePictureData;

    public AddUserView() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3("Información del usuario");

        // Formulario
        FormLayout formLayout2Col = new FormLayout();
        TextField nameField = new TextField("Nombre");
        TextField usernameField = new TextField("Nombre de usuario");
        PasswordField passwordField = new PasswordField("Contraseña");
        ComboBox<String> roleComboBox = new ComboBox<>("Tipo de usuario");
        EmailField emailField = new EmailField("Email");

        roleComboBox.setItems("USER", "ADMIN", "OTP", "CIO", "PROMOTOR");
        roleComboBox.setPlaceholder("Seleccione un rol");

        // Avatar y botón para subir imagen
        avatar = new Avatar();
        avatar.setName("Vista previa");

        Button uploadButton = new Button("Subir imagen de perfil", event -> openFileUploadDialog());

        // Botón de guardar
        Button saveButton = new Button("Guardar");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> saveUser(
                nameField.getValue(),
                usernameField.getValue(),
                passwordField.getValue(),
                roleComboBox.getValue(),
                emailField.getValue()
        ));

        // Layout de botones
        HorizontalLayout layoutRow = new HorizontalLayout(uploadButton, saveButton);
        layoutRow.setWidthFull();
        layoutRow.setJustifyContentMode(JustifyContentMode.BETWEEN);

        // Diseño principal
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");

        formLayout2Col.setWidth("100%");
        formLayout2Col.add(nameField, usernameField, passwordField, roleComboBox, emailField);

        layoutColumn2.add(h3, formLayout2Col, avatar, layoutRow);
        getContent().add(layoutColumn2);

        getContent().setWidthFull();
        getContent().setAlignItems(Alignment.CENTER);
        getContent().setJustifyContentMode(JustifyContentMode.START);
    }

    private void openFileUploadDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Subir imagen de perfil");

        Button uploadButton = new Button("Seleccionar archivo", e -> {
            // Aquí se integraría la lógica para subir la imagen
            profilePictureData = new byte[]{/* imagen simulada */};
            avatar.setImageResource(new StreamResource("avatar.png", () -> new ByteArrayInputStream(profilePictureData)));
            Notification.show("Imagen cargada correctamente");
            dialog.close();
        });

        dialog.add(uploadButton);
        dialog.open();
    }

    private void saveUser(String name, String username, String password, String role, String email) {
        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || role == null || email.isEmpty()) {
            Notification.show("Por favor, complete todos los campos.", 3000, Notification.Position.MIDDLE);
            return;
        }

        // Lógica para guardar en la base de datos se implementará aquí
        Notification.show("Usuario guardado con éxito.", 3000, Notification.Position.MIDDLE);
    }
}
