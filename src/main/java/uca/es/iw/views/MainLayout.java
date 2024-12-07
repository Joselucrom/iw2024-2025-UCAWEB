package uca.es.iw.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import uca.es.iw.data.User;
import uca.es.iw.security.AuthenticatedUser;

/**
 * The main view is a top-level placeholder for other views.
 */
@Layout
@AnonymousAllowed
public class MainLayout extends AppLayout {

    private H1 viewTitle;
    private final AuthenticatedUser authenticatedUser;
    private final AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
        addLanguageSwitcher(); // Añadimos los botones de cambio de idioma
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        Span appName = new Span("IW");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        List<MenuEntry> menuEntries = MenuConfiguration.getMenuEntries();
        menuEntries.forEach(entry -> {
            if (entry.icon() != null) {
                nav.addItem(new SideNavItem(entry.title(), entry.path(), new SvgIcon(entry.icon())));
            } else {
                nav.addItem(new SideNavItem(entry.title(), entry.path()));
            }
        });

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            Avatar avatar = new Avatar(user.getName());
            StreamResource resource = new StreamResource("profile-pic",
                    () -> new ByteArrayInputStream(user.getProfilePicture()));
            avatar.setImageResource(resource);
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(user.getName());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);

            // Obtener las traducciones para "Ver perfil" y "Cerrar sesión"
            String viewProfileText = getTranslation("menu.viewProfile");
            String logoutText = getTranslation("menu.logout");
            // Usar las traducciones
            userName.getSubMenu().addItem(viewProfileText, e -> {
                getUI().ifPresent(ui -> ui.navigate("perfil"));
            });
            userName.getSubMenu().addItem(logoutText, e -> {
                authenticatedUser.logout();
            });
            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }

    private void addLanguageSwitcher() {
        // Obtener las traducciones para los botones de idioma
        String languageText = getTranslation("menu.language");
        String spanishText = getTranslation("menu.spanish");
        String englishText = getTranslation("menu.english");

        // Crear el MenuBar para los idiomas
        MenuBar languageMenu = new MenuBar();

        // Crear el "MenuItem" que servirá como el botón principal del menú con el icono de flecha hacia abajo
        MenuItem languageMenuItem = languageMenu.addItem(languageText);

        // Añadir un icono de flecha hacia abajo al "MenuItem"
        languageMenuItem.getElement().appendChild(new Icon("lumo", "dropdown").getElement());

        // Crear los ítems del submenú para cambiar el idioma
        languageMenuItem.getSubMenu().addItem(spanishText, e -> switchLanguage(new Locale("es")));
        languageMenuItem.getSubMenu().addItem(englishText, e -> switchLanguage(new Locale("en")));

        // Añadir el MenuBar a la barra de navegación
        HorizontalLayout languageSwitcher = new HorizontalLayout(languageMenu);
        languageSwitcher.setPadding(true);
        languageSwitcher.getStyle().set("margin-left", "auto"); // Alinea los botones a la derecha
        addToNavbar(languageSwitcher);

        // Establecer el idioma predeterminado al cargar la página
        Locale currentLocale = VaadinSession.getCurrent().getLocale();
        if (currentLocale == null) {
            // Si no hay un idioma en la sesión, establecer inglés como predeterminado
            VaadinSession.getCurrent().setLocale(Locale.ENGLISH);
        }
    }


    private void switchLanguage(Locale locale) {
        VaadinSession.getCurrent().setLocale(locale);
        VaadinService.getCurrentRequest().getWrappedSession().setAttribute("locale", locale);

        // Asegurarse de recargar la UI
        getUI().ifPresent(ui -> ui.access(() -> {
            ui.getPage().reload();
        }));
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();

        // Obtener el título dinámico desde la vista actual
        String pageTitle = getTranslation(getCurrentPageTitle());
        viewTitle.setText(pageTitle);
        getUI().ifPresent(ui -> ui.getPage().setTitle(pageTitle));
    }

    private String getCurrentPageTitle() {
        // Usa un método en las vistas para proporcionar claves de traducción
        String className = getContent().getClass().getSimpleName();
        switch (className) {
            case "PerfilView":
                return "perfil.titulo";
            default:
                return "app.default_title"; // Título predeterminado
        }
    }

}