package org.evgenysav.infrastructure.core.impl;

import org.evgenysav.infrastructure.core.Scanner;
import org.reflections.Reflections;

import java.util.Collections;
import java.util.Set;

public class ScannerImpl implements Scanner {

    private Reflections reflections;

    /**
     * @param packageName the name of package to scan
     */
    public ScannerImpl(String packageName) {
        reflections = new Reflections(packageName);
    }

    /**
     * @param type to find subtypes of
     * @return Set<Class < ? extends T>> if type has subtypes
     * @return empty set if type doesn't have subtypes
     * @implNote <p> If type doesn't have subtypes <p> then it returns Collections.emptySet
     */
    @Override
    public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> type) {
        Set<Class<? extends T>> subTypesOf = reflections.getSubTypesOf(type);
        if (!subTypesOf.isEmpty()) {
            return subTypesOf;
        }

        return Collections.emptySet();
    }

    @Override
    public Reflections getReflections() {
        return reflections;
    }
}
