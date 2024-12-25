package uca.es.iw.views.evaluation;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import uca.es.iw.services.ProyectoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@PageTitle("Evaluación de oportunidad")
@Route("oportunityevaluation")
@Menu(order = 8, icon = "line-awesome/svg/clipboard-list-solid.svg")
@RolesAllowed("CIO")
public class OportunityEvaluationView extends Composite<VerticalLayout> {
    ProyectoService proyectoService;

    public OportunityEvaluationView(ProyectoService proyectoService) {
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

        // Añadir Span para mostrar la calificación de oportunidad
        Span calificacionOportunidadLabel = new Span("Calificación de oportunidad: Sin calificar");
        calificacionOportunidadLabel.setVisible(false);  // Inicialmente oculto

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
                calificacionOportunidadLabel.setVisible(true);

                // Obtener la calificación de oportunidad desde el servicio (si existe)
                Integer calOportunidad = proyectoService.getCalificacionOportunidad(selectedProject);
                if (calOportunidad != null) {
                    calificacionOportunidadLabel.setText("Calificación de oportunidad: " + calOportunidad);
                } else {
                    calificacionOportunidadLabel.setText("Calificación de oportunidad: Sin calificar");
                }
            } else {
                // Ocultar el Span si no hay proyecto seleccionado
                calificacionOportunidadLabel.setVisible(false);
            }
        });

        downloadButton.addClickListener(e -> {
            String selectedProject = comboBox.getValue(); // Obtiene el proyecto seleccionado actual
            if (selectedProject != null) {
                String downloadUrl = proyectoService.getDownloadUrl(selectedProject, 1);
                getUI().ifPresent(ui -> ui.getPage().open(downloadUrl));
            }
        });

        h2.setText("Necesidad y urgencia");
        layoutColumn2.setAlignSelf(FlexComponent.Alignment.CENTER, h2);
        h2.setWidth("max-content");

        // Añadir campos de evaluación para "Necesidad y urgencia"
        ComboBox<Integer> necesidadImportancia = new ComboBox<>("Importancia de la necesidad");
        necesidadImportancia.setItems(getNumericOptions());
        ComboBox<Integer> necesidadUrgencia = new ComboBox<>("Urgencia de la necesidad");
        necesidadUrgencia.setItems(getNumericOptions());

        h22.setText("Alineación con los objetivos estratégicos");
        layoutColumn2.setAlignSelf(FlexComponent.Alignment.CENTER, h22);
        h22.setWidth("max-content");

        // Añadir checkbox group para "Alineación con los objetivos estratégicos"
        CheckboxGroup<String> checkboxGroup = new CheckboxGroup<>();
        checkboxGroup.setLabel("Alineamiento con los objetivos estratégicos:");
        checkboxGroup.setWidth("600px");
        checkboxGroup.setItems(
                "Innovar, rediseñar y actualizar nuestra oferta formativa para adaptarla a las necesidades sociales y económicas de nuestro entorno.",
                "Conseguir los niveles más altos de calidad en nuestra oferta formativa propia y reglada.",
                "Aumentar significativamente nuestro posicionamiento en investigación y transferir de forma relevante y útil nuestra investigación a nuestro tejido social y productivo.",
                "Consolidar un modelo de gobierno sostenible y socialmente responsable.",
                "Conseguir que la transparencia sea un valor distintivo y relevante en la UCA.",
                "Generar valor compartido con la Comunidad Universitaria.",
                "Reforzar la importancia del papel de la UCA en la sociedad."
        );
        checkboxGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);

        // Más campos de evaluación...
        // Impacto y beneficios
        ComboBox<Integer> impactoBeneficios = new ComboBox<>("Impacto y beneficios");
        impactoBeneficios.setItems(getNumericOptions());

        // Riesgos y complejidad
        ComboBox<Integer> riesgosComplejidad = new ComboBox<>("Riesgos y complejidad");
        riesgosComplejidad.setItems(getNumericOptions());

        // Memoria e hitos
        ComboBox<Integer> memoriaCompleta = new ComboBox<>("Calidad de la memoria");
        memoriaCompleta.setItems(getNumericOptions());
        ComboBox<Integer> hitosClaros = new ComboBox<>("Definición de hitos claros");
        hitosClaros.setItems(getNumericOptions());

        layoutRow.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(LumoUtility.Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");

        buttonPrimary.setText("Guardar");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonPrimary.addClickListener(e -> {
            String selectedProject = comboBox.getValue();

            // Obtener los valores de todas las subcategorías
            Integer importancia = necesidadImportancia.getValue();
            Integer urgencia = necesidadUrgencia.getValue();
            Integer impacto = impactoBeneficios.getValue();
            Integer riesgos = riesgosComplejidad.getValue();
            Integer memoria = memoriaCompleta.getValue();
            Integer hitos = hitosClaros.getValue();
            Set<String> objetivosSeleccionados = checkboxGroup.getSelectedItems();

            // Validar que se hayan seleccionado todos los campos
            if (selectedProject == null) {
                Notification.show("Por favor, selecciona un proyecto.", 3000, Notification.Position.MIDDLE);
                return;
            }

            if (importancia == null || urgencia == null || impacto == null || riesgos == null || memoria == null || hitos == null) {
                Notification.show("Por favor, completa todas las calificaciones.", 3000, Notification.Position.MIDDLE);
                return;
            }

            // Calcular la calificación de oportunidad
            int calOportunidad = importancia + urgencia + impacto + riesgos + memoria + hitos + objetivosSeleccionados.size();

            try {
                proyectoService.updateCalOportunidad(selectedProject, calOportunidad);
                Notification.show("Calificación de oportunidad actualizada correctamente.", 3000, Notification.Position.MIDDLE);

                // Limpiar los campos después de la acción
                comboBox.clear();
                necesidadImportancia.clear();
                necesidadUrgencia.clear();
                impactoBeneficios.clear();
                riesgosComplejidad.clear();
                memoriaCompleta.clear();
                hitosClaros.clear();
                checkboxGroup.clear();
                calificacionOportunidadLabel.setVisible(false); // Ocultar el Span después de guardar
            } catch (Exception ex) {
                Notification.show("Error al actualizar la calificación: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        getContent().add(layoutColumn2);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(comboBox);
        layoutColumn2.add(downloadButton);
        layoutColumn2.add(calificacionOportunidadLabel);
        layoutColumn2.add(hr);
        layoutColumn2.add(h2);
        layoutColumn2.add(hr2);
        layoutColumn2.add(necesidadImportancia);
        layoutColumn2.add(necesidadUrgencia);
        layoutColumn2.add(hr3);
        layoutColumn2.add(h22);
        layoutColumn2.add(hr4);
        layoutColumn2.add(checkboxGroup);
        layoutColumn2.add(impactoBeneficios);
        layoutColumn2.add(riesgosComplejidad);
        layoutColumn2.add(memoriaCompleta);
        layoutColumn2.add(hitosClaros);
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

