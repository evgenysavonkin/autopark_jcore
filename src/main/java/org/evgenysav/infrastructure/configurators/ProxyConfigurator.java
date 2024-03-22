package org.evgenysav.infrastructure.configurators;

import org.evgenysav.infrastructure.core.Context;

public interface ProxyConfigurator {
    <T> T makeProxy(T object, Class<T> implementation, Context context);
}
