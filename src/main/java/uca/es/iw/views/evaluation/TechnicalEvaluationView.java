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

        Button downloadButton2 = new Button("Descargar documento especificaciones técnicas");
        downloadButton2.setEnabled(false); // Inicialmente deshabilitado
        downloadButton2.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

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

        // Añadir subcategorías para la calidad del producto
        ComboBox<Integer> calidadAdecuacionFuncional = new ComboBox<>("Adecuación Funcional");
        calidadAdecuacionFuncional.setItems(getNumericOptions());
        ComboBox<Integer> calidadMantenibilidad = new ComboBox<>("Mantenibilidad");
        calidadMantenibilidad.setItems(getNumericOptions());
        ComboBox<Integer> calidadPortabilidad = new ComboBox<>("Portabilidad");
        calidadPortabilidad.setItems(getNumericOptions());
        ComboBox<Integer> calidadEficiencia = new ComboBox<>("Eficiencia");
        calidadEficiencia.setItems(getNumericOptions());
        ComboBox<Integer> calidadUsabilidad = new ComboBox<>("Usabilidad");
        calidadUsabilidad.setItems(getNumericOptions());
        ComboBox<Integer> calidadCompatibilidad = new ComboBox<>("Compatibilidad");
        calidadCompatibilidad.setItems(getNumericOptions());
        ComboBox<Integer> calidadSeguridad = new ComboBox<>("Seguridad");
        calidadSeguridad.setItems(getNumericOptions());

        h22.setText("Gestión y soporte");
        layoutColumn2.setAlignSelf(Alignment.CENTER, h22);
        h22.setWidth("max-content");

        // Añadir subcategorías para la gestión y soporte
        ComboBox<Integer> gestionGarantia = new ComboBox<>("Garantía");
        gestionGarantia.setItems(getNumericOptions());
        ComboBox<Integer> gestionTiempoRespuesta = new ComboBox<>("Tiempos de Respuesta");
        gestionTiempoRespuesta.setItems(getNumericOptions());
        ComboBox<Integer> gestionAtencionCliente = new ComboBox<>("Atención al Cliente");
        gestionAtencionCliente.setItems(getNumericOptions());
        ComboBox<Integer> gestionDocumentacion = new ComboBox<>("Documentación Técnica");
        gestionDocumentacion.setItems(getNumericOptions());

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

            // Obtener los valores de todas las subcategorías
            Integer calAdecuacion = calidadAdecuacionFuncional.getValue();
            Integer calMantenibilidad = calidadMantenibilidad.getValue();
            Integer calPortabilidad = calidadPortabilidad.getValue();
            Integer calEficiencia = calidadEficiencia.getValue();
            Integer calUsabilidad = calidadUsabilidad.getValue();
            Integer calCompatibilidad = calidadCompatibilidad.getValue();
            Integer calSeguridad = calidadSeguridad.getValue();

            Integer gesGarantia = gestionGarantia.getValue();
            Integer gesTiempoRespuesta = gestionTiempoRespuesta.getValue();
            Integer gesAtencionCliente = gestionAtencionCliente.getValue();
            Integer gesDocumentacion = gestionDocumentacion.getValue();

            // Validar que se hayan seleccionado todos los campos
            if (selectedProject == null) {
                Notification.show("Por favor, selecciona un proyecto.", 3000, Notification.Position.MIDDLE);
                return;
            }

            // Validar que todas las subcategorías tengan valores
            if (calAdecuacion == null || calMantenibilidad == null || calPortabilidad == null ||
                    calEficiencia == null || calUsabilidad == null || calCompatibilidad == null || calSeguridad == null ||
                    gesGarantia == null || gesTiempoRespuesta == null || gesAtencionCliente == null || gesDocumentacion == null) {
                Notification.show("Por favor, completa todas las calificaciones.", 3000, Notification.Position.MIDDLE);
                return;
            }

            // Calcular la suma total de las calificaciones
            int calTecnica = calAdecuacion + calMantenibilidad + calPortabilidad + calEficiencia + calUsabilidad +
                    calCompatibilidad + calSeguridad + gesGarantia + gesTiempoRespuesta + gesAtencionCliente + gesDocumentacion;

            try {
                proyectoService.updateCalTecnica(selectedProject, calTecnica);
                Notification.show("Calificación técnica actualizada correctamente.", 3000, Notification.Position.MIDDLE);

                // Limpiar los campos después de la acción
                comboBox.clear();
                calidadAdecuacionFuncional.clear();
                calidadMantenibilidad.clear();
                calidadPortabilidad.clear();
                calidadEficiencia.clear();
                calidadUsabilidad.clear();
                calidadCompatibilidad.clear();
                calidadSeguridad.clear();
                gestionGarantia.clear();
                gestionTiempoRespuesta.clear();
                gestionAtencionCliente.clear();
                gestionDocumentacion.clear();
                calificacionTecnicaLabel.setVisible(false); // Ocultar el Span de calificación después de la acción
            } catch (Exception ex) {
                Notification.show("Error al actualizar la calificación: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        getContent().add(layoutColumn2);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(comboBox);
        layoutColumn2.add(downloadButton); // Añadir botón de descarga debajo del ComboBox
        layoutColumn2.add(calificacionTecnicaLabel); // Añadir label de calificación técnica
        layoutColumn2.add(hr);
        layoutColumn2.add(h2);
        layoutColumn2.add(hr2);
        layoutColumn2.add(calidadAdecuacionFuncional);
        layoutColumn2.add(calidadMantenibilidad);
        layoutColumn2.add(calidadPortabilidad);
        layoutColumn2.add(calidadEficiencia);
        layoutColumn2.add(calidadUsabilidad);
        layoutColumn2.add(calidadCompatibilidad);
        layoutColumn2.add(calidadSeguridad);
        layoutColumn2.add(hr3);
        layoutColumn2.add(h22);
        layoutColumn2.add(hr4);
        layoutColumn2.add(gestionGarantia);
        layoutColumn2.add(gestionTiempoRespuesta);
        layoutColumn2.add(gestionAtencionCliente);
        layoutColumn2.add(gestionDocumentacion);
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
