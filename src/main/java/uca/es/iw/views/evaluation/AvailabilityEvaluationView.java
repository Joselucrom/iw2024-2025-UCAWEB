package uca.es.iw.views.evaluation;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import uca.es.iw.services.ProyectoService;
import uca.es.iw.services.RecursosService;

import static org.apache.commons.lang3.math.NumberUtils.max;

@PageTitle("Evaluación financiera")
@Route("financialevaluation")
@Menu(order = 9, icon = "line-awesome/svg/money-bill-solid.svg")
@RolesAllowed({"CIO", "OTP"})
public class AvailabilityEvaluationView extends Composite<VerticalLayout> {

    @ClientCallable
    public void showNotification(String message) {
        Notification.show(message, 3000, Notification.Position.MIDDLE);
    }

    private final ProyectoService proyectoService;
    private final RecursosService recursosService;

    public AvailabilityEvaluationView(ProyectoService proyectoService, RecursosService recursosService) {
        this.proyectoService = proyectoService;
        this.recursosService = recursosService;

        VerticalLayout layoutColumn2 = new VerticalLayout();
        FormLayout formLayout2Col = new FormLayout();
        ComboBox<String> comboBox = new ComboBox<>();
        Hr hr = new Hr();
        H2 h2 = new H2("Datos financieros");
        Hr hr2 = new Hr();

        // Botones de descarga
        Button downloadMemoryButton = new Button("Descargar memoria");
        Button downloadSpecsButton = new Button("Descargar especificaciones técnicas");
        Button downloadBudgetButton = new Button("Descargar presupuestos");

        downloadMemoryButton.setEnabled(false);
        downloadSpecsButton.setEnabled(false);
        downloadBudgetButton.setEnabled(false);

        downloadMemoryButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        downloadSpecsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        downloadBudgetButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // ComboBox para selección de proyectos
        comboBox.setLabel("Selecciona un proyecto para evaluar");
        comboBox.setWidth("min-content");

        proyectoService.setSelectProjects(comboBox);

        // Campos de recursos humanos y presupuesto
        NumberField recursosHumanos = new NumberField();
        recursosHumanos.setLabel("Número estimado de recursos humanos");
        recursosHumanos.setWidth("300px");
        NumberField presupuestoEstimado = new NumberField();
        presupuestoEstimado.setLabel("Presupuesto estimado en euros");
        presupuestoEstimado.setWidth("300px");


        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);

        layoutColumn2.setWidthFull();
        getContent().setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");

        formLayout2Col.setWidth("100%");
        // Notificación de calificación
        Span calificacionLabel = new Span("Calificación de disponibilidad: Sin calificar");
        calificacionLabel.setVisible(false);

        comboBox.addValueChangeListener(event -> {
            String selectedProject = event.getValue();
            downloadMemoryButton.setEnabled(selectedProject != null); // Activa o desactiva el botón
            downloadSpecsButton.setEnabled(selectedProject != null); // Activa o desactiva el botón
            downloadBudgetButton.setEnabled(selectedProject != null); // Activa o desactiva el botón

            if (selectedProject != null) {
                // Mostrar el Span
                calificacionLabel.setVisible(true);

                // Obtener la calificación de oportunidad desde el servicio (si existe)
                Double calDisponibilidad = proyectoService.getCalificacionDisponibilidad(selectedProject);
                if (calDisponibilidad != null) {
                    calificacionLabel.setText("Calificación de oportunidad: " + calDisponibilidad);
                } else {
                    calificacionLabel.setText("Calificación de oportunidad: Sin calificar");
                }
            } else {
                // Ocultar el Span si no hay proyecto seleccionado
                calificacionLabel.setVisible(false);
            }
        });

        downloadMemoryButton.addClickListener(e -> {
            String selectedProject = comboBox.getValue(); // Obtiene el proyecto seleccionado actual
            if (selectedProject != null) {
                String downloadUrl = proyectoService.getDownloadUrl(selectedProject, 1);
                // Validar si el archivo existe antes de abrir la pestaña
                UI.getCurrent().getPage().executeJs(
                        "fetch($0, { method: 'HEAD' }).then(response => { " +
                                "if (response.ok) {" +
                                "    window.open($0, '_blank');" +
                                "} else {" +
                                "    $1.$server.showNotification('La memoria no está disponible para el proyecto seleccionado.');" +
                                "}});",
                        downloadUrl, getElement());
            }
        });

        downloadSpecsButton.addClickListener(e -> {
            String selectedProject = comboBox.getValue(); // Obtiene el proyecto seleccionado actual
            if (selectedProject != null) {
                String downloadUrl = proyectoService.getDownloadUrl(selectedProject, 2);
                // Validar si el archivo existe antes de abrir la pestaña
                UI.getCurrent().getPage().executeJs(
                        "fetch($0, { method: 'HEAD' }).then(response => { " +
                                "if (response.ok) {" +
                                "    window.open($0, '_blank');" +
                                "} else {" +
                                "    $1.$server.showNotification('Las especificaciones no están disponibles para el proyecto seleccionado.');" +
                                "}});",
                        downloadUrl, getElement());
            }
        });

        downloadBudgetButton.addClickListener(e -> {
            String selectedProject = comboBox.getValue(); // Obtiene el proyecto seleccionado actual
            if (selectedProject != null) {
                String downloadUrl = proyectoService.getDownloadUrl(selectedProject, 3);
                // Validar si el archivo existe antes de abrir la pestaña
                UI.getCurrent().getPage().executeJs(
                        "fetch($0, { method: 'HEAD' }).then(response => { " +
                                "if (response.ok) {" +
                                "    window.open($0, '_blank');" +
                                "} else {" +
                                "    $1.$server.showNotification('Los presupuestos no están disponibles para el proyecto seleccionado.');" +
                                "}});",
                        downloadUrl, getElement());
            }
        });

        // Botón de guardar calificación
        Button saveButton = new Button("Guardar");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        saveButton.addClickListener(e -> {
            String selectedProject = comboBox.getValue();
            if (selectedProject == null) {
                Notification.show("Por favor, selecciona un proyecto.", 3000, Notification.Position.MIDDLE);
                return;
            }

            try {
                // Obtener valores
                double recursosDouble = recursosHumanos.getValue(); // Obtener el valor directamente como double
                int recursos = (int) recursosDouble; // Convertir a int si es necesario
                double presupuesto = presupuestoEstimado.getValue(); // Obtener el valor directamente como double
                double presupuestoTotal = recursosService.getPresupuestoTotal();
                double financiacionAportada = proyectoService.getFinanciacionAportada(selectedProject);

                presupuesto -= financiacionAportada;

                double puntuacionRecursos = calcularPuntuacionRecursos(recursos);
                double puntuacionPresupuesto = calcularPuntuacionPresupuesto(presupuesto, presupuestoTotal);

                double calificacion = (puntuacionRecursos + puntuacionPresupuesto);

                // Guardar calificación
                proyectoService.updateCalDisponibilidad(selectedProject, calificacion, recursos, max(0, presupuesto));
                Notification.show("Calificación de disponibilidad guardada correctamente.", 3000, Notification.Position.MIDDLE);

                // Mostrar la calificación en el Span
                calificacionLabel.setText("Calificación de disponibilidad: " + calificacion);
                calificacionLabel.setVisible(true);

                // Limpiar campos
                comboBox.clear();
                recursosHumanos.clear();
                presupuestoEstimado.clear();
            } catch (Exception ex) {
                Notification.show("Error al guardar la calificación: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

            getContent().add(layoutColumn2);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(comboBox);
        layoutColumn2.add(downloadMemoryButton, downloadSpecsButton, downloadBudgetButton);
        layoutColumn2.add(calificacionLabel);
        layoutColumn2.add(hr);
        layoutColumn2.add(h2);
        layoutColumn2.add(hr2);
        layoutColumn2.add(recursosHumanos);
        layoutColumn2.add(presupuestoEstimado);
        layoutColumn2.add(saveButton);
    }

    private double calcularPuntuacionRecursos(int recursos) {
        if (recursos >= 11) return 0;
        double calrec = Math.max(0, 11 - recursos);
        return calrec / 2;
    }

    private double calcularPuntuacionPresupuesto(double presupuesto, double presupuestoTotal) {
        double porcentaje = (presupuesto / presupuestoTotal) * 100;
        if (presupuesto <= 0) return 5; // Financiación suficiente
        if (porcentaje < 3) return 5;
        if (porcentaje < 7) return 4;
        if (porcentaje < 10) return 3;
        if (porcentaje < 15) return 2;
        if (porcentaje < 20) return 1;
        return 0;
    }
}
