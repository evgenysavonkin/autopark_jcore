package org.evgenysav.infrastructure.core;

import org.evgenysav.infrastructure.config.Config;

public interface Context {
    <T> T getObject(Class<T> type);

    Config getConfig();
}
