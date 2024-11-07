package uca.es.IW;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "view2", layout = MainView.class)
public class View2 extends VerticalLayout {

    public View2() {
        H2 titulo = new H2("Esta es la segunda vista");
        add(titulo);
    }
}
