package uca.es.IW;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("")
public class MainView extends AppLayout {

	public MainView() {
		// Toggle para abrir y cerrar el menú
		DrawerToggle toggle = new DrawerToggle();

		// Encabezado de la página principal
		setPrimarySection(Section.DRAWER);
		addToNavbar(toggle);

		// Menú de navegación lateral
		VerticalLayout menuLayout = new VerticalLayout();
		menuLayout.add(new RouterLink("Home", HomeView.class));
		menuLayout.add(new RouterLink("Vista 2", View2.class));
		menuLayout.add(new RouterLink("Vista 3", View3.class));

		// Agregar el menú al drawer
		addToDrawer(menuLayout);

		// Contenido principal de la página (por defecto)
		setContent(new HomeView());
	}
}
