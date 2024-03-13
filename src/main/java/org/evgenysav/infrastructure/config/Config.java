package org.evgenysav.infrastructure.config;

import org.evgenysav.infrastructure.core.Scanner;

public interface Config {
    <T> Class<? extends T> getImplementation(Class<T> target);
    Scanner getScanner();
}
