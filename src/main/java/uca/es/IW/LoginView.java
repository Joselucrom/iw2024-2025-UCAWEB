package uca.es.IW;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("login")
public class LoginView extends VerticalLayout {

    public LoginView() {
        // Título
        setSizeFull();
        FormLayout formLayout = new FormLayout();

        // Campos de texto para usuario y contraseña
        TextField usernameField = new TextField("Username");
        PasswordField passwordField = new PasswordField("Password");

        // Botón para iniciar sesión
        Button loginButton = new Button("Login", event -> login(usernameField.getValue(), passwordField.getValue()));

        formLayout.add(usernameField, passwordField, loginButton);
        add(formLayout);
    }

    private void login(String username, String password) {
        // Aquí deberías agregar la lógica para autenticar al usuario
        // Verificar que el usuario y la contraseña coincidan en la base de datos
        System.out.println("Usuario: " + username + ", Contraseña: " + password);
        // Si la autenticación es exitosa, redirigir a otra vista, por ejemplo:
        // getUI().ifPresent(ui -> ui.navigate(HomeView.class));
    }
}
