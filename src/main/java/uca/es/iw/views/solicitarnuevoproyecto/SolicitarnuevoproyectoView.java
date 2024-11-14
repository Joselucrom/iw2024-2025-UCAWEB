package uca.es.iw.views.solicitarnuevoproyecto;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import uca.es.iw.data.Proyecto;
import uca.es.iw.services.ProyectoService;
import java.time.LocalDate;

@PageTitle("Solicitar nuevo proyecto")
@Route("person-form")
@Menu(order = 1, icon = "line-awesome/svg/user.svg")
@RolesAllowed("USER")
public class SolicitarnuevoproyectoView extends Composite<VerticalLayout> {

    private final ProyectoService proyectoService;

    public SolicitarnuevoproyectoView(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;

        TextField tituloField = new TextField("Título del proyecto");
        TextArea descripcionArea = new TextArea("Descripción del proyecto");
        TextArea justificacionArea = new TextArea("Justificación del proyecto");
        TextArea metasArea = new TextArea("Metas y objetivo");
        TextArea impactoArea = new TextArea("Impacto esperado");
        TextArea alcanceArea = new TextArea("Alcance del proyecto");
        TextArea reqFuncionalesArea = new TextArea("Requisitos funcionales");
        TextArea reqNoFuncionalesArea = new TextArea("Requisitos no funcionales");
        TextArea comentariosArea = new TextArea("Comentarios adicionales");

        Button buttonPrimary = new Button("Enviar petición");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Configuración de diseño
        getContent().setWidth("100%");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);

        tituloField.setWidth("100%");
        descripcionArea.setWidth("100%");
        justificacionArea.setWidth("100%");
        metasArea.setWidth("100%");
        impactoArea.setWidth("100%");
        alcanceArea.setWidth("100%");
        reqFuncionalesArea.setWidth("100%");
        reqNoFuncionalesArea.setWidth("100%");
        comentariosArea.setWidth("100%");

        // Acción del botón
        buttonPrimary.addClickListener(event -> {
            Proyecto proyecto = new Proyecto();
            proyecto.setTitulo(tituloField.getValue());
            proyecto.setDescripcion(descripcionArea.getValue());
            proyecto.setJustificacion(justificacionArea.getValue());
            proyecto.setMetasObjetivo(metasArea.getValue());
            proyecto.setImpactoEsperado(impactoArea.getValue());
            proyecto.setAlcance(alcanceArea.getValue());
            proyecto.setRequisitosFuncionales(reqFuncionalesArea.getValue());
            proyecto.setRequisitosNoFuncionales(reqNoFuncionalesArea.getValue());
            proyecto.setComentariosAdicionales(comentariosArea.getValue());
            proyecto.setFechaSolicitud(LocalDate.now());
            proyecto.setEstado("Pendiente");

            proyectoService.guardarProyecto(proyecto);

            Notification.show("Proyecto enviado con éxito");

            // Limpiar campos después de enviar
            tituloField.clear();
            descripcionArea.clear();
            justificacionArea.clear();
            metasArea.clear();
            impactoArea.clear();
            alcanceArea.clear();
            reqFuncionalesArea.clear();
            reqNoFuncionalesArea.clear();
            comentariosArea.clear();
        });

        // Agregar componentes al layout
        getContent().add(tituloField, descripcionArea, justificacionArea, metasArea, impactoArea, alcanceArea, reqFuncionalesArea, reqNoFuncionalesArea, comentariosArea, buttonPrimary);
    }
}
