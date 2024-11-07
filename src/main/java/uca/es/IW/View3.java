package uca.es.IW;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "view3", layout = MainView.class)
public class View3 extends VerticalLayout {

    public View3() {
        H2 titulo = new H2("Esta es la tercera vista");
        add(titulo);
    }
}
