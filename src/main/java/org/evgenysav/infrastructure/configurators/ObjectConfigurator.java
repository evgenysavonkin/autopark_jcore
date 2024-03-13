package org.evgenysav.infrastructure.configurators;

import org.evgenysav.infrastructure.core.Context;

public interface ObjectConfigurator {
    void configure(Object object, Context context);
}
