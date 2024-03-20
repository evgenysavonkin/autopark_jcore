package org.evgenysav.infrastructure.config.impl;

import lombok.AllArgsConstructor;
import org.evgenysav.infrastructure.config.Config;
import org.evgenysav.infrastructure.core.Scanner;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
public class JavaConfig implements Config {

    private final Scanner scanner;

    private final Map<Class<?>, Class<?>> interfaceToImplementation;

    @SuppressWarnings("unchecked")
    @Override
    public <T> Class<? extends T> getImplementation(Class<T> target) {
        Optional<? extends Class<?>> optional = interfaceToImplementation.entrySet().stream()
                .filter(entry -> entry.getKey().getSimpleName().equals(target.getSimpleName()))
                .map(Map.Entry::getValue)
                .findFirst();

        if (optional.isEmpty()) {
            System.out.println("Target interface " + target.getSimpleName() + " wasn't found. Trying to find implementation automatically");
        } else {
            return (Class<? extends T>) optional.get();
        }

        Set<Class<?>> subTypesOf = scanner.getSubTypesOf((Class<Object>) target);

        if (subTypesOf.isEmpty()) {
            return target;
        }

        if (subTypesOf.size() == 1) {
            for (var type : subTypesOf) {
                return (Class<? extends T>) type;
            }
        }

        throw new RuntimeException("Target interface " + target + " has 0 or more than one impl");
    }

    @Override
    public Scanner getScanner() {
        return scanner;
    }
}
