package uca.es.iw.views.selection;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import uca.es.iw.data.Convocatoria;
import uca.es.iw.data.Proyecto;
import uca.es.iw.services.ProyectoService;
import uca.es.iw.services.RecursosService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PageTitle("Selección de Proyectos")
@Route("project-selection")
@Menu(order = 11, icon = "line-awesome/svg/tasks-solid.svg")
@RolesAllowed({"CIO"})
public class ProjectSelectionView extends VerticalLayout {

    private final ProyectoService proyectoService;
    private final RecursosService recursosService;

    private final ComboBox<Convocatoria> convocatoriaComboBox = new ComboBox<>("Seleccionar Convocatoria");
    private final Span financiacionRestante = new Span("Presupuesto restante: 0.00 €");
    private final Span recursosRestantes = new Span("Recursos humanos restantes: 0");

    private final Grid<Proyecto> projectGrid = new Grid<>(Proyecto.class, false); // Mover Grid aquí

    private double totalFinanciacionRestante = 0.0;
    private int totalRecursosRestantes = 0;
    private final Map<Long, String> selectedStatuses = new HashMap<>();

    public ProjectSelectionView(ProyectoService proyectoService, RecursosService recursosService) {
        this.proyectoService = proyectoService;
        this.recursosService = recursosService;

        setWidth("100%");
        setAlignItems(FlexComponent.Alignment.CENTER);

        H2 title = new H2("Selección de Proyectos");
        add(title);

        // Configuración del ComboBox
        convocatoriaComboBox.setItemLabelGenerator(Convocatoria::getNombre);
        convocatoriaComboBox.setItems(proyectoService.getAllConvocatorias());
        convocatoriaComboBox.addValueChangeListener(event -> loadProjects(event.getValue()));
        add(convocatoriaComboBox);

        // Configuración del Grid
        configureProjectGrid();
        add(projectGrid);

        // Layout para estadísticas
        HorizontalLayout statsLayout = new HorizontalLayout(financiacionRestante, recursosRestantes);
        statsLayout.setWidthFull();
        statsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.AROUND);
        add(statsLayout);

        // Botón para guardar cambios
        Button saveButton = new Button("Guardar Cambios", event -> saveChanges());
        add(saveButton);

        // Cargar proyectos iniciales
        loadProjects(null);
    }

    private void configureProjectGrid() {
        projectGrid.setSelectionMode(SelectionMode.NONE);
        projectGrid.setWidthFull();

        projectGrid.addColumn(new ComponentRenderer<>(proyecto -> {
            Anchor anchor = new Anchor(String.format("project-view/%s", proyecto.getId()), proyecto.getNombreCorto());
            anchor.getElement().setAttribute("theme", "tertiary");
            return anchor;
        })).setHeader("Título").setAutoWidth(true);

        projectGrid.addColumn(Proyecto::getFinanciacionNecesaria).setHeader("Presupuesto necesario").setSortable(true);
        projectGrid.addColumn(Proyecto::getRecursosHumanosNecesarios).setHeader("RRHH necesarios").setSortable(true);
        projectGrid.addColumn(Proyecto::getCalFinal).setHeader("Calificación Final").setSortable(true);

        projectGrid.addComponentColumn(project -> {
            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.setItems("Pendiente", "Aceptado", "Rechazado");
            comboBox.setValue(getProjectState(project));

            comboBox.addValueChangeListener(event -> {
                updateProjectState(project, event.getOldValue(), event.getValue());
                updateStats();
            });

            return comboBox;
        }).setHeader("Estado");
    }

    private void loadProjects(Convocatoria convocatoria) {
        List<Proyecto> proyectos;
        if (convocatoria == null) {
            proyectos = proyectoService.searchQualifiedProjects();
        } else {
            proyectos = proyectoService.searchProjectsByConvocatoria(convocatoria);
        }
        projectGrid.setItems(proyectos);

        totalFinanciacionRestante = recursosService.getPresupuestoRestante();
        totalRecursosRestantes = recursosService.getRecursosHumanosRestantes();

        updateStats();
    }

    private String getProjectState(Proyecto project) {
        return project.getEstado();
    }

    private void updateProjectState(Proyecto project, String oldState, String newState) {
        if ("Aceptado".equals(newState) && !"Aceptado".equals(oldState)) {
            totalFinanciacionRestante -= project.getFinanciacionNecesaria();
            totalRecursosRestantes -= project.getRecursosHumanosNecesarios();
        } else if ("Rechazado".equals(newState) && "Aceptado".equals(oldState)) {
            totalFinanciacionRestante += project.getFinanciacionNecesaria();
            totalRecursosRestantes += project.getRecursosHumanosNecesarios();
        } else if ("Pendiente".equals(newState) && "Aceptado".equals(oldState)) {
            totalFinanciacionRestante += project.getFinanciacionNecesaria();
            totalRecursosRestantes += project.getRecursosHumanosNecesarios();
        }

        selectedStatuses.put(project.getId(), newState);
    }

    private void updateStats() {
        financiacionRestante.setText(String.format("Financiación restante: %.2f €", totalFinanciacionRestante));
        recursosRestantes.setText("Recursos humanos restantes: " + totalRecursosRestantes);
    }

    private void saveChanges() {
        if (selectedStatuses.isEmpty()) {
            Notification.show("No se han realizado cambios", 3000, Notification.Position.MIDDLE);
            return;
        }
        if (totalFinanciacionRestante < 0) {
            Notification.show("No hay suficiente financiación", 5000, Notification.Position.MIDDLE);
            return;
        }
        if (totalRecursosRestantes < 0) {
            Notification.show("No hay suficientes recursos humanos", 5000, Notification.Position.MIDDLE);
            return;
        }

        try {
            projectGrid.getDataProvider().fetch(new com.vaadin.flow.data.provider.Query<>())
                    .forEach(project -> {
                        String newStatus = selectedStatuses.get(project.getId());
                        if (newStatus != null) {
                            proyectoService.updateProjectStatus(project, newStatus);
                        }
                    });
            recursosService.updateRecursosRestantes(totalFinanciacionRestante, totalRecursosRestantes);
            Notification.show("Cambios guardados correctamente", 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("Error al guardar los cambios: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }
}