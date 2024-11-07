package uca.es.IW;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "home", layout = MainView.class)
public class HomeView extends VerticalLayout {

    public HomeView() {
        H1 titulo = new H1("Bienvenido a la página principal");
        add(titulo);
    }
}