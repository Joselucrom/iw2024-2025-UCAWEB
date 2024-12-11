package uca.es.iw.views.explorarproyectos;

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
import com.vaadin.flow.component.textfield.TextArea;
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
    private TextField solicitante;
    private ComboBox<String> estado;
    private DatePicker fechaSolicitud;

    private final Button cancel = new Button("Cancelar");
    private final Button save = new Button("Guardar");

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
        splitLayout.setSplitterPosition(70);

        add(splitLayout);

        // Configure Grid
        grid.addColumn(new ComponentRenderer<>(proyecto -> {
            if (proyecto instanceof Proyecto) {
                Anchor anchor = new Anchor(String.format("project-view/%s", ((Proyecto)proyecto).getId()), proyecto.getNombreCorto());
                anchor.getElement().setAttribute("theme", "tertiary");
                return anchor;
            }
            return null;
                })).setHeader("Título").setAutoWidth(true);
        grid.addColumn("nombreSolicitante").setHeader("Solicitante").setAutoWidth(true);
        grid.addColumn("estado").setHeader("Estado").setAutoWidth(true);
        grid.addColumn("fechaCreado").setHeader("Fecha de Solicitud").setAutoWidth(true);

        grid.setItems(query -> proyectoService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        /*grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                //UI.getCurrent().navigate(String.format(PROYECTO_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
                UI.getCurrent().navigate("/project-view/" + event.getValue().getId());
            } else {
                clearForm();
                UI.getCurrent().navigate(ExplorarProyectosView.class);
            }
        });*/

        // Configure Form
        binder = new BeanValidationBinder<>(Proyecto.class);

        // Bind fields
        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            titulo.clear();
            solicitante.clear();
            estado.clear();
            fechaSolicitud.clear();

            grid.setItems(proyectoService.searchProjects("", "", "", null));
        });

        save.addClickListener(e -> {
            String tituloText = titulo.getValue();
            String solicitanteText = solicitante.getValue();
            String estadoText = estado.getValue();
            DatePicker fechaSolicitudText = fechaSolicitud;

            grid.setItems(proyectoService.searchProjects(tituloText, solicitanteText, estadoText, fechaSolicitudText));
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
        solicitante = new TextField("Usuario solicitante");
        estado = new ComboBox<>("Estado");
        estado.setItems("Pendiente", "Aprobado", "Rechazado");
        fechaSolicitud = new DatePicker("Fecha de Solicitud");
        formLayout.add(titulo, solicitante, estado, fechaSolicitud);

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
