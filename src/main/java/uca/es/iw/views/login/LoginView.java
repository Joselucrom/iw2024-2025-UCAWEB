package uca.es.iw.views.login;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import uca.es.iw.security.AuthenticatedUser;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")

public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final AuthenticatedUser authenticatedUser;

    public LoginView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        var login = new LoginForm();
        login.setAction("login");
        login.setForgotPasswordButtonVisible(false);
        add(
                login,
                new Anchor("register", "Register")
        );


    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authenticatedUser.get().isPresent()) {
            // Already logged in
            event.forwardTo("");
        }
    }

}
