package uca.es.iw.views.gestionarconvocatorias;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import uca.es.iw.data.Convocatoria;
import uca.es.iw.services.ConvocatoriaService;
import uca.es.iw.views.MainLayout;
import uca.es.iw.views.modconvocatoria.ModifyConvocatoriaView;

import java.util.List;

@Route(value = "convocatoria-management", layout = MainLayout.class)
@Menu(order = 2, icon = "line-awesome/svg/calendar.svg")
@RolesAllowed("ADMIN")
public class ConvocatoriaView extends VerticalLayout {

    private final ConvocatoriaService convocatoriaService;

    private final Grid<Convocatoria> convocatoriaGrid = new Grid<>(Convocatoria.class, false);
    private final TextField nombreField = new TextField("Nombre de la convocatoria");
    private final TextField objetivoField = new TextField("Objetivo");
    private final DatePicker fechaInicioField = new DatePicker("Fecha de inicio");
    private final DatePicker fechaFinField = new DatePicker("Fecha de fin");
    private final NumberField presupuestoField = new NumberField("Presupuesto (€)");
    private final NumberField recursosHumanosField = new NumberField("Recursos Humanos");
    private final Button saveButton = new Button("Guardar", event -> saveConvocatoria());
    private final Button clearButton = new Button("Limpiar", event -> clearForm());

    private Long editingId = null; // Modo edición

    @Autowired
    public ConvocatoriaView(ConvocatoriaService convocatoriaService) {
        this.convocatoriaService = convocatoriaService;

        // Configuración de diseño
        setWidth("100%");
        setAlignItems(FlexComponent.Alignment.CENTER);

        // Título
        H3 title = new H3("Gestión de Convocatorias");
        add(title);

        // Campos del formulario
        nombreField.setWidth("100%");
        objetivoField.setWidth("100%");
        fechaInicioField.setWidth("100%");
        fechaFinField.setWidth("100%");
        presupuestoField.setWidth("100%");
        recursosHumanosField.setWidth("100%");

        // Diseño del formulario
        add(nombreField, objetivoField, fechaInicioField, fechaFinField, presupuestoField, recursosHumanosField, saveButton, clearButton);

        // Configuración del grid
        configureGrid();
        convocatoriaGrid.setWidthFull();
        add(convocatoriaGrid);

        // Cargar convocatorias
        loadConvocatorias();
    }

    private void configureGrid() {
        convocatoriaGrid.addColumn(Convocatoria::getNombre).setHeader("Nombre").setSortable(true);
        convocatoriaGrid.addColumn(Convocatoria::getObjetivo).setHeader("Objetivo").setSortable(true);
        convocatoriaGrid.addColumn(Convocatoria::getFechaApertura).setHeader("Fecha de Inicio").setSortable(true);
        convocatoriaGrid.addColumn(Convocatoria::getFechaCierre).setHeader("Fecha de Fin").setSortable(true);
        convocatoriaGrid.addColumn(Convocatoria::getPresupuestoTotal).setHeader("Presupuesto (€)").setSortable(true);
        convocatoriaGrid.addColumn(Convocatoria::getCupoRecursosHumanos).setHeader("RRHH").setSortable(true);
        convocatoriaGrid.addComponentColumn(convocatoria -> {
            Button editButton = new Button("Editar", event -> {
                UI.getCurrent().navigate(ModifyConvocatoriaView.class, convocatoria.getId());
            });
            Button deleteButton = new Button("Eliminar", event -> deleteConvocatoria(convocatoria));
            return new VerticalLayout(editButton, deleteButton);
        }).setHeader("Acciones");
    }


    private void loadConvocatorias() {
        List<Convocatoria> convocatorias = convocatoriaService.getAllConvocatorias();
        convocatoriaGrid.setItems(convocatorias);
    }

    private void saveConvocatoria() {
        try {
            if (editingId == null) {
                convocatoriaService.createConvocatoria(
                        nombreField.getValue(),
                        objetivoField.getValue(),
                        fechaInicioField.getValue(),
                        fechaFinField.getValue(),
                        presupuestoField.getValue(),
                        recursosHumanosField.getValue().intValue()
                );
                Notification.show("Convocatoria creada con éxito.");
            } else {
                convocatoriaService.updateConvocatoria(
                        editingId,
                        nombreField.getValue(),
                        objetivoField.getValue(),
                        fechaInicioField.getValue(),
                        fechaFinField.getValue(),
                        presupuestoField.getValue(),
                        recursosHumanosField.getValue().intValue()
                );
                Notification.show("Convocatoria actualizada con éxito.");
                editingId = null;
            }
            clearForm();
            loadConvocatorias();
        } catch (Exception e) {
            Notification.show("Error al guardar la convocatoria: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    private void editConvocatoria(Convocatoria convocatoria) {
        editingId = convocatoria.getId();
        nombreField.setValue(convocatoria.getNombre());
        objetivoField.setValue(convocatoria.getObjetivo());
        fechaInicioField.setValue(convocatoria.getFechaApertura());
        fechaFinField.setValue(convocatoria.getFechaCierre());
        presupuestoField.setValue(convocatoria.getPresupuestoTotal());
        recursosHumanosField.setValue((double) convocatoria.getCupoRecursosHumanos());
    }

    private void deleteConvocatoria(Convocatoria convocatoria) {
        try {
            convocatoriaService.deleteConvocatoria(convocatoria.getId());
            Notification.show("Convocatoria eliminada con éxito.");
            loadConvocatorias();
        } catch (Exception e) {
            Notification.show("Error al eliminar la convocatoria: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    private void clearForm() {
        editingId = null;
        nombreField.clear();
        objetivoField.clear();
        fechaInicioField.clear();
        fechaFinField.clear();
        presupuestoField.clear();
        recursosHumanosField.clear();
    }
}