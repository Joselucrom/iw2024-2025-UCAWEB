package uca.es.iw.views.searchusers;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.PermitAll;
import java.util.Optional;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import uca.es.iw.data.User;
import uca.es.iw.services.UserService;

@PageTitle("Buscar Usuarios")
@Route("search-users/:userID?/:action?(edit)")
@Menu(order = 5, icon = "line-awesome/svg/user-solid.svg")
@RolesAllowed("ADMIN")
@Uses(Icon.class)
public class SearchUsersView extends Div implements BeforeEnterObserver {

    private final String id = "userID";
    private final String USER_EDIT_ROUTE_TEMPLATE = "buscar-usuarios/%s/edit";

    private final Grid<User> grid = new Grid<>(User.class, false);

    private TextField nombre;
    private EmailField email;
    private ComboBox<String> rol;

    private final Button cancel = new Button("Cancelar");
    private final Button save = new Button("Guardar");

    private final BeanValidationBinder<User> binder;

    private User user;

    private final UserService userService;

    public SearchUsersView(UserService userService) {

        this.userService = userService;
        addClassNames("buscar-usuarios-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);
        splitLayout.setSplitterPosition(70);

        add(splitLayout);

        grid.addColumn(new ComponentRenderer<>(user -> {
            if (user instanceof User) { // Extra seguridad para evitar errores de inferencia.
                Anchor anchor = new Anchor(String.format("modify-user/%s", ( user).getId()), user.getUsername());
                anchor.getElement().setAttribute("theme", "tertiary");
                return anchor;
            }
            return null; // En caso de que no sea un usuario válido.
        })).setHeader("Nombre de usuario").setAutoWidth(true);
        grid.addColumn("email").setHeader("Correo Electrónico").setAutoWidth(true);
        grid.addColumn("roles").setHeader("Rol").setAutoWidth(true);

        grid.setItems(query -> userService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // When a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(USER_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(SearchUsersView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(User.class);

        // Bind fields
        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            // Limpiar los campos de búsqueda
            nombre.clear();
            email.clear();

            // Restablecer el Grid con todos los usuarios
            grid.setItems(userService.searchUsers("", ""));
        });

        save.addClickListener(e -> {
            String nombreValue = nombre.getValue();
            String emailValue = email.getValue();

            // Filtrar usuarios
            grid.setItems(userService.searchUsers(nombreValue, emailValue));
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> userId = event.getRouteParameters().get(id).map(Long::parseLong);
        if (userId.isPresent()) {
            Optional<User> userFromBackend = userService.get(userId.get());
            if (userFromBackend.isPresent()) {
                populateForm(userFromBackend.get());
            } else {
                Notification.show(
                        String.format("No se encontró el usuario solicitado, ID = %s", userId.get()),
                        3000, Notification.Position.BOTTOM_START);
                refreshGrid();
                event.forwardTo(SearchUsersView.class);
            }
        }
    }



    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        nombre = new TextField("Nombre");
        email = new EmailField("Correo Electrónico");


        formLayout.add(nombre, email);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }


    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(User value) {
        this.user = value;
        binder.readBean(this.user);
    }


}
