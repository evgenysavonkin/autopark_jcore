package org.evgenysav.infrastructure.core.impl;

import org.evgenysav.infrastructure.core.Scanner;
import org.reflections.Reflections;

import java.util.Collections;
import java.util.Set;

public class ScannerImpl implements Scanner {

    private Reflections reflections;

    public ScannerImpl(String packageName) {
        reflections = new Reflections(packageName);
    }

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
