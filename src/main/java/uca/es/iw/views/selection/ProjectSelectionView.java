package uca.es.iw.views.selection;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
//import org.springframework.data.jpa.repository.Query;
import uca.es.iw.data.Proyecto;
import uca.es.iw.data.Recursos;
import uca.es.iw.services.ProyectoService;
import uca.es.iw.services.RecursosService;
import com.vaadin.flow.data.provider.Query;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PageTitle("Selección de Proyectos")
@Route("project-selection")
@Menu(order = 11, icon = "line-awesome/svg/tasks-solid.svg")
@RolesAllowed({"CIO"})
public class ProjectSelectionView extends Composite<VerticalLayout> {

    private final ProyectoService proyectoService;
    private final RecursosService recursosService;

    private final Span financiacionRestante = new Span("Presupuesto restante: 0.00 €");
    private final Span recursosRestantes = new Span("Recursos humanos restantes: 0");

    private double totalFinanciacionRestante = 0.0;
    private int totalRecursosRestantes = 0;

    private final Map<Long, String> selectedStatuses = new HashMap<>();


    public ProjectSelectionView(ProyectoService proyectoService, RecursosService recursosService) {
        this.proyectoService = proyectoService;
        this.recursosService = recursosService;

        VerticalLayout content = getContent();
        content.setWidth("100%");
        content.setAlignItems(FlexComponent.Alignment.CENTER);

        H2 title = new H2("Selección de Proyectos");
        content.add(title);

        // Tabla de proyectos
        Grid<Proyecto> projectGrid = new Grid<>(Proyecto.class, false);
        projectGrid.setSelectionMode(SelectionMode.NONE);
        projectGrid.setWidthFull();

        projectGrid.addColumn(Proyecto::getNombreCorto).setHeader("Nombre").setSortable(true);
        projectGrid.addColumn(Proyecto::getFinanciacionNecesaria).setHeader("Presupuesto necesario").setSortable(true);
        projectGrid.addColumn(Proyecto::getRecursosHumanosNecesarios).setHeader("RRHH necesarios").setSortable(true);
        projectGrid.addColumn(Proyecto::getCalFinal).setHeader("Calificación Final").setSortable(true);

        // ComboBox en cada fila
        projectGrid.addComponentColumn(project -> {
            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.setItems("Pendiente", "Aceptado", "Rechazado");
            comboBox.setValue(getProjectState(project)); // Mostrar el estado inicial del proyecto
            comboBox.setId("combo-" + project.getId()); // Asignar un ID único al ComboBox

            // Lógica de actualización
            comboBox.addValueChangeListener(event -> {
                String oldValue = event.getOldValue();
                String newValue = event.getValue();

                if ("Aceptado".equals(newValue)) {
                    if (!"Aceptado".equals(oldValue)) {
                        totalFinanciacionRestante -= project.getFinanciacionNecesaria();
                        totalRecursosRestantes -= project.getRecursosHumanosNecesarios();
                    }
                } else if ("Rechazado".equals(newValue)) {
                    if ("Aceptado".equals(oldValue)) {
                        totalFinanciacionRestante += project.getFinanciacionNecesaria();
                        totalRecursosRestantes += project.getRecursosHumanosNecesarios();
                    }
                } else if ("Pendiente".equals(newValue)) {
                    if ("Aceptado".equals(oldValue)) {
                        totalFinanciacionRestante += project.getFinanciacionNecesaria();
                        totalRecursosRestantes += project.getRecursosHumanosNecesarios();
                    }
                }
                selectedStatuses.put(project.getId(), event.getValue());
                updateStats();
            });

            return comboBox;
        }).setHeader("Estado").setKey("Estado");

        content.add(projectGrid);

        // Estadísticas
        HorizontalLayout statsLayout = new HorizontalLayout(financiacionRestante, recursosRestantes);
        statsLayout.setWidthFull();
        statsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.AROUND);

        content.add(statsLayout);

        // Botón de guardar
        Button saveButton = new Button("Guardar Cambios", event -> {
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
                // Obtener los proyectos de la tabla
                List<Proyecto> proyectos = projectGrid.getDataProvider().fetch(new Query<>()).toList();

                for (Proyecto proyecto : proyectos) {
                    // Obtener el estado seleccionado desde una estructura asociada
                    String newStatus = selectedStatuses.get(proyecto.getId());
                    if (newStatus != null) {
                        proyectoService.updateProjectStatus(proyecto, newStatus);
                    }
                }

                // Guardar recursos restantes
                recursosService.updateRecursosRestantes(totalFinanciacionRestante, totalRecursosRestantes);

                Notification.show("Cambios guardados correctamente", 3000, Notification.Position.MIDDLE);
            } catch (Exception e) {
                Notification.show("Error al guardar los cambios: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });



        content.add(saveButton);

        // Cargar datos en la tabla
        loadProjects(projectGrid);
    }

    // Obtener el estado actual del proyecto
    private String getProjectState(Proyecto project) {
        return project.getEstado();
    }

    private void loadProjects(Grid<Proyecto> projectGrid) {
        List<Proyecto> proyectos = proyectoService.searchQualifiedProjects();
        projectGrid.setItems(proyectos);

        // Calcular valores iniciales
        totalFinanciacionRestante = recursosService.getPresupuestoRestante();
        totalRecursosRestantes = recursosService.getRecursosHumanosRestantes();

        updateStats();
    }

    private void updateStats() {
        financiacionRestante.setText(String.format("Financiación restante: %.2f €", totalFinanciacionRestante));
        recursosRestantes.setText("Recursos humanos restantes: " + totalRecursosRestantes);
    }
}