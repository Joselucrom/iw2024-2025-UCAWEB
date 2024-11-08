package uca.es.IW;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;

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
		menuLayout.setSpacing(false);  // Sin espaciado entre los elementos del menú

		// Elementos del menú
		menuLayout.add(new RouterLink("Home", HomeView.class));
		menuLayout.add(new RouterLink("Vista 2", View2.class));
		menuLayout.add(new RouterLink("Vista 3", View3.class));

		// Crear el botón de login
		Button loginButton = new Button("Login", VaadinIcon.USER.create());
		loginButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("login")));

		// Crear un layout para el botón de login
		VerticalLayout bottomLayout = new VerticalLayout();
		bottomLayout.add(loginButton);
		bottomLayout.setWidthFull();  // Asegura que el layout ocupe todo el ancho disponible

		// Añadir un espaciador (espacio flexible) entre el contenido superior y el botón de login
		VerticalLayout spacerLayout = new VerticalLayout();  // Espaciador vacío
		spacerLayout.setFlexGrow(1); // Asegura que este espacio crezca para empujar el botón de login hacia abajo

		// Agregar espaciador al layout del menú
		menuLayout.add(spacerLayout);

		// Coloca el botón de login al final del layout
		menuLayout.add(bottomLayout);

		// Asegura que los elementos del menú estén alineados al principio (arriba)
		menuLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

		// Agregar el menú con el botón de login al drawer
		addToDrawer(menuLayout);

		// Contenido principal de la página
		setContent(new HomeView());
	}
}
