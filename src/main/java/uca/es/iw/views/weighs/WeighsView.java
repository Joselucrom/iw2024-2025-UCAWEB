package uca.es.iw.views.weighs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import uca.es.iw.data.Ponderaciones;
import jakarta.annotation.security.RolesAllowed;
import uca.es.iw.services.ProyectoService;
import uca.es.iw.services.WeighsService;

@PageTitle("Editar Ponderaciones")
@Route("editar-ponderaciones")
@Menu(order = 10, icon = "line-awesome/svg/line-awesome/edit.svg")
@RolesAllowed({"CIO", "OTP"})
public class WeighsView extends VerticalLayout {

    private final WeighsService weighsService;
    private final ProyectoService proyectoService;

    public WeighsView(WeighsService weighsService, ProyectoService proyectoService) {
        this.weighsService = weighsService;
        this.proyectoService = proyectoService;

        setSpacing(true);
        setPadding(true);
        setAlignItems(Alignment.CENTER);

        // Cargar ponderaciones desde la base de datos
        Ponderaciones ponderaciones = weighsService.findById(1);

        // Campos para las ponderaciones
        NumberField ponTecnicaField = new NumberField("Ponderación Técnica");
        ponTecnicaField.setValue(ponderaciones.getPonTecnica() != null ? ponderaciones.getPonTecnica() : 0.0);

        NumberField ponDisponibilidadField = new NumberField("Ponderación Disponibilidad");
        ponDisponibilidadField.setValue(ponderaciones.getPonDisponibilidad() != null ? ponderaciones.getPonDisponibilidad() : 0.0);

        NumberField ponOportunidadField = new NumberField("Ponderación Oportunidad");
        ponOportunidadField.setValue(ponderaciones.getPonOportunidad() != null ? ponderaciones.getPonOportunidad() : 0.0);

        // Botón de guardar
        Button guardarButton = new Button("Guardar");
        guardarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        guardarButton.addClickListener(event -> {
            if (ponTecnicaField.getValue() == null || ponDisponibilidadField.getValue() == null || ponOportunidadField.getValue() == null) {
                Notification.show("Ninguno de los campos puede estar vacío", 3000, Notification.Position.MIDDLE);
                return;
            }

            // Obtener valores ingresados
            double ponTecnica = ponTecnicaField.getValue();
            double ponDisponibilidad = ponDisponibilidadField.getValue();
            double ponOportunidad = ponOportunidadField.getValue();

            // Validar que la suma de las ponderaciones sea 1
            double suma = ponTecnica + ponDisponibilidad + ponOportunidad;
            if (Math.abs(suma - 1.0) > 0.001) { // Permitir un margen de error pequeño
                Notification.show("La suma de las ponderaciones debe ser igual a 1.", 3000, Notification.Position.MIDDLE);
                return;
            }

            if (ponTecnica < 0 || ponDisponibilidad < 0 || ponOportunidad < 0) {
                Notification.show("Las ponderaciones no pueden ser negativas.", 3000, Notification.Position.MIDDLE);
                return;
            }

            weighsService.updateWeighs(ponTecnica, ponDisponibilidad, ponOportunidad);
            proyectoService.updateAllCalFinal();
            Notification.show("Ponderaciones guardadas correctamente.", 3000, Notification.Position.MIDDLE);
        });

        add(ponTecnicaField, ponDisponibilidadField, ponOportunidadField, guardarButton);
    }
}
