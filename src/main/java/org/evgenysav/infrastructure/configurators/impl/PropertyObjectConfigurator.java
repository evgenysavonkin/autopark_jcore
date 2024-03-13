package org.evgenysav.infrastructure.configurators.impl;

import lombok.SneakyThrows;
import org.evgenysav.infrastructure.configurators.ObjectConfigurator;
import org.evgenysav.infrastructure.core.Context;
import org.evgenysav.infrastructure.core.annotations.Property;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PropertyObjectConfigurator implements ObjectConfigurator {

    private final Map<String, String> properties;

    @SneakyThrows
    public PropertyObjectConfigurator() {
        URL path = this.getClass().getClassLoader().getResource("application.properties");
        if (path == null) {
            throw new FileNotFoundException(String.format("File '%s' not found", "application.properties"));
        }

        Stream<String> lines;
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(path.openStream())
        )) {
            lines = bufferedReader.lines();
            properties = lines.map(line -> line.split("="))
                    .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));
        }
    }

    @SneakyThrows
    @Override
    public void configure(Object t, Context context) {
        Class<?> clazz = t.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Property.class)) {
                Property propertyAnnotation = field.getAnnotation(Property.class);
                String propertyValue = propertyAnnotation.value();
                field.setAccessible(true);
                if (!propertyValue.isEmpty()) {
                    setPropertyToObject(t, field, propertyValue);
                } else {
                    setPropertyToObject(t, field, t.toString());
                }
            }
        }
    }

    private void setPropertyToObject(Object t, Field field, String propertyValue) throws IllegalAccessException {
        Optional<String> valueOfPropertyKey = properties.entrySet().stream()
                .filter(entry -> entry.getKey().equals(propertyValue))
                .map(Map.Entry::getValue)
                .findFirst();

        if (valueOfPropertyKey.isPresent()) {
            field.set(t, valueOfPropertyKey.get());
        }
    }
}
