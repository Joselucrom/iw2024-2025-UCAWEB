package uca.es.iw.views.evaluation;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import jakarta.annotation.security.RolesAllowed;
import uca.es.iw.services.ProyectoService;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Evaluación técnica")
@Route("technicalevaluation")
@Menu(order = 7, icon = "line-awesome/svg/clipboard-list-solid.svg")
@RolesAllowed("OTP")
public class TechnicalEvaluationView extends Composite<VerticalLayout> {
    ProyectoService proyectoService;

    public TechnicalEvaluationView(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;

        VerticalLayout layoutColumn2 = new VerticalLayout();
        FormLayout formLayout2Col = new FormLayout();
        ComboBox<String> comboBox = new ComboBox<>();
        Hr hr = new Hr();
        H2 h2 = new H2();
        Hr hr2 = new Hr();
        ComboBox<Integer> comboBox2 = new ComboBox<>();
        Hr hr3 = new Hr();
        H2 h22 = new H2();
        Hr hr4 = new Hr();
        ComboBox<Integer> comboBox3 = new ComboBox<>();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button buttonPrimary = new Button();

        // Nuevo botón de descarga
        Button downloadButton = new Button("Descargar memoria");
        downloadButton.setEnabled(false); // Inicialmente deshabilitado
        downloadButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // Añadir Span para mostrar la calificación técnica
        Span calificacionTecnicaLabel = new Span("Calificación técnica: Sin calificar");
        calificacionTecnicaLabel.setVisible(false);  // Inicialmente oculto

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);

        layoutColumn2.setWidthFull();
        getContent().setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");

        formLayout2Col.setWidth("100%");
        comboBox.setLabel("Selecciona un proyecto para evaluar");
        comboBox.setWidth("min-content");

        // Llenar ComboBox con datos de proyectos
        proyectoService.setSelectProjects(comboBox);

        // Evento de selección de ComboBox
        comboBox.addValueChangeListener(event -> {
            String selectedProject = event.getValue();
            downloadButton.setEnabled(selectedProject != null); // Activa o desactiva el botón

            if (selectedProject != null) {
                // Mostrar el Span
                calificacionTecnicaLabel.setVisible(true);

                // Obtener la calificación técnica desde el servicio (si existe)
                Integer calTecnica = proyectoService.getCalificacionTecnica(selectedProject);
                if (calTecnica != null) {
                    calificacionTecnicaLabel.setText("Calificación técnica: " + calTecnica);
                } else {
                    calificacionTecnicaLabel.setText("Calificación técnica: Sin calificar");
                }
            } else {
                // Ocultar el Span si no hay proyecto seleccionado
                calificacionTecnicaLabel.setVisible(false);
            }
        });

        downloadButton.addClickListener(e -> {
            String selectedProject = comboBox.getValue(); // Obtiene el proyecto seleccionado actual
            if (selectedProject != null) {
                String downloadUrl = proyectoService.getDownloadUrl(selectedProject);
                getUI().ifPresent(ui -> ui.getPage().open(downloadUrl));
            }
        });

        h2.setText("Calidad del producto");
        layoutColumn2.setAlignSelf(Alignment.CENTER, h2);
        h2.setWidth("max-content");

        comboBox2.setLabel("Seleccionar puntuación de la calidad del producto");
        comboBox2.setWidth("400px");
        comboBox2.setItems(getNumericOptions());

        h22.setText("Gestión y soporte");
        layoutColumn2.setAlignSelf(Alignment.CENTER, h22);
        h22.setWidth("max-content");

        comboBox3.setLabel("Seleccionar puntuación de la gestión y soporte");
        comboBox3.setWidth("400px");
        comboBox3.setItems(getNumericOptions());

        layoutRow.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");

        buttonPrimary.setText("Guardar");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonPrimary.addClickListener(e -> {
            String selectedProject = comboBox.getValue();
            Integer calidadProducto = comboBox2.getValue();
            Integer gestionSoporte = comboBox3.getValue();

            // Validar que se hayan seleccionado todos los campos
            if (selectedProject == null) {
                Notification.show("Por favor, selecciona un proyecto.", 3000, Notification.Position.MIDDLE);
                return;
            }
            if (calidadProducto == null || gestionSoporte == null) {
                Notification.show("Por favor, selecciona valores para las calificaciones.", 3000, Notification.Position.MIDDLE);
                return;
            }

            // Calcular la suma y actualizar el proyecto
            int calTecnica = calidadProducto + gestionSoporte;

            try {
                proyectoService.updateCalTecnica(selectedProject, calTecnica);
                Notification.show("Calificación técnica actualizada correctamente.", 3000, Notification.Position.MIDDLE);

                // Limpiar los campos después de la acción
                comboBox.clear();
                comboBox2.clear();
                comboBox3.clear();
                calificacionTecnicaLabel.setVisible(false); // Ocultar el Span de calificación después de la acción
            } catch (Exception ex) {
                Notification.show("Error al actualizar la calificación: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        getContent().add(layoutColumn2);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(comboBox);
        layoutColumn2.add(calificacionTecnicaLabel);
        layoutColumn2.add(downloadButton);
        layoutColumn2.add(hr);
        layoutColumn2.add(h2);
        layoutColumn2.add(hr2);
        layoutColumn2.add(comboBox2);
        layoutColumn2.add(hr3);
        layoutColumn2.add(h22);
        layoutColumn2.add(hr4);
        layoutColumn2.add(comboBox3);
        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonPrimary);
    }

    private List<Integer> getNumericOptions() {
        int start = 0;
        int end = 10;
        List<Integer> options = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            options.add(i);
        }
        return options;
    }
}

