package uca.es.iw.views.solicitarnuevoproyecto;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Solicitar nuevo proyecto")
@Route("person-form")
@Menu(order = 1, icon = "line-awesome/svg/user.svg")
@RolesAllowed("USER")
public class SolicitarnuevoproyectoView extends Composite<VerticalLayout> {

    public SolicitarnuevoproyectoView() {
        TextField textField = new TextField();
        TextArea textArea = new TextArea();
        TextArea textArea2 = new TextArea();
        TextArea textArea3 = new TextArea();
        TextArea textArea4 = new TextArea();
        TextArea textArea5 = new TextArea();
        TextArea textArea6 = new TextArea();
        TextArea textArea7 = new TextArea();
        TextArea textArea8 = new TextArea();
        Button buttonPrimary = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        textField.setLabel("Título del proyecto");
        textField.setWidth("100%");
        textArea.setLabel("Descripción del proyecto");
        textArea.setWidth("100%");
        textArea2.setLabel("Justificación del proyecto");
        textArea2.setWidth("100%");
        textArea3.setLabel("Metas y objetivo");
        textArea3.setWidth("100%");
        textArea4.setLabel("Impacto esperado");
        textArea4.setWidth("100%");
        textArea5.setLabel("Alcance del proyecto");
        textArea5.setWidth("100%");
        textArea6.setLabel("Requisitos funcionales");
        textArea6.setWidth("100%");
        textArea7.setLabel("Requisitos no funcionales");
        textArea7.setWidth("100%");
        textArea8.setLabel("Comentarios adicionales");
        textArea8.setWidth("100%");
        buttonPrimary.setText("Enviar petición");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getContent().add(textField);
        getContent().add(textArea);
        getContent().add(textArea2);
        getContent().add(textArea3);
        getContent().add(textArea4);
        getContent().add(textArea5);
        getContent().add(textArea6);
        getContent().add(textArea7);
        getContent().add(textArea8);
        getContent().add(buttonPrimary);
    }
}
