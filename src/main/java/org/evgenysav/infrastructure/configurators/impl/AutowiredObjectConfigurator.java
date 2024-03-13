package org.evgenysav.infrastructure.configurators.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.evgenysav.infrastructure.configurators.ObjectConfigurator;
import org.evgenysav.infrastructure.core.Context;
import org.evgenysav.infrastructure.core.annotations.Autowired;

import java.lang.reflect.Field;

@AllArgsConstructor
@NoArgsConstructor
public class AutowiredObjectConfigurator implements ObjectConfigurator {

    @Autowired
    private Context context;

    @SneakyThrows
    @Override
    public void configure(Object t, Context context) {
        this.context = context;
        Class<?> clazz = t.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                field.set(t, context.getObject(field.getType()));
            }
        }
    }
}
