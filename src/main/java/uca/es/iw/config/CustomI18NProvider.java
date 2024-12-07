package uca.es.iw.config;

import com.vaadin.flow.i18n.I18NProvider;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Primary;

import java.text.MessageFormat;
import java.util.*;

@Primary
@Component
public class CustomI18NProvider implements I18NProvider {

    private static final String BUNDLE_PREFIX = "translate";
    private static final List<Locale> SUPPORTED_LOCALES = Arrays.asList(new Locale("es"), Locale.ENGLISH);

    @Override
    public List<Locale> getProvidedLocales() {
        return SUPPORTED_LOCALES;
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_PREFIX, locale);
            String translation = bundle.getString(key);
            return MessageFormat.format(translation, params);
        } catch (MissingResourceException e) {
            return key; // Devuelve la clave si no se encuentra la traducción
        }
    }
}