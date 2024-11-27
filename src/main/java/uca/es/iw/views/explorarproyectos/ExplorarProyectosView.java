package uca.es.iw.views.explorarproyectos;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.PermitAll;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import uca.es.iw.data.Proyecto;
import uca.es.iw.services.ProyectoService;

@PageTitle("Explorar Proyectos")
@Route("explorar-proyectos/:proyectoID?/:action?(edit)")
@Menu(order = 2, icon = "line-awesome/svg/columns-solid.svg")
@PermitAll
@Uses(Icon.class)
public class ExplorarProyectosView extends Div implements BeforeEnterObserver {

    private final String ProyectoId = "proyectoID";
    private final String PROYECTO_EDIT_ROUTE_TEMPLATE = "explorar-proyectos/%s/edit";

    private final Grid<Proyecto> grid = new Grid<>(Proyecto.class, false);

    private TextField titulo;
    private TextArea descripcion;
    private TextField estado;
    private DatePicker fechaSolicitud;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<Proyecto> binder;

    private Proyecto proyecto;

    private final ProyectoService proyectoService;

    public ExplorarProyectosView(ProyectoService proyectoService) {

        this.proyectoService = proyectoService;
        addClassNames("explorar-proyectos-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("titulo").setHeader("Título").setAutoWidth(true);
        grid.addColumn("descripcion").setHeader("Descripción").setAutoWidth(true);
        grid.addColumn("estado").setHeader("Estado").setAutoWidth(true);
        grid.addColumn("fechaSolicitud").setHeader("Fecha de Solicitud").setAutoWidth(true);

        grid.setItems(query -> proyectoService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(PROYECTO_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ExplorarProyectosView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Proyecto.class);

        // Bind fields
        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.proyecto == null) {
                    this.proyecto = new Proyecto();
                }
                binder.writeBean(this.proyecto);
                proyectoService.update(this.proyecto);
                clearForm();
                refreshGrid();
                Notification.show("Proyecto actualizado con éxito");
                UI.getCurrent().navigate(ExplorarProyectosView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error al actualizar el proyecto. Otra persona ha modificado el registro.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("No se pudo actualizar el proyecto. Revisa que los valores sean válidos.");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> proyectoId = event.getRouteParameters().get(ProyectoId).map(Long::parseLong);
        if (proyectoId.isPresent()) {
            Optional<Proyecto> proyectoFromBackend = proyectoService.get(proyectoId.get());
            if (proyectoFromBackend.isPresent()) {
                populateForm(proyectoFromBackend.get());
            } else {
                Notification.show(
                        String.format("No se encontró el proyecto solicitado, ID = %s", proyectoId.get()),
                        3000, Notification.Position.BOTTOM_START);
                refreshGrid();
                event.forwardTo(ExplorarProyectosView.class);
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
        titulo = new TextField("Título");
        descripcion = new TextArea("Descripción");
        estado = new TextField("Estado");
        fechaSolicitud = new DatePicker("Fecha de Solicitud");
        formLayout.add(titulo, descripcion, estado, fechaSolicitud);

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

    private void populateForm(Proyecto value) {
        this.proyecto = value;
        binder.readBean(this.proyecto);
    }
}
