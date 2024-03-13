package org.evgenysav.infrastructure.core.impl;

import org.evgenysav.infrastructure.config.Config;
import org.evgenysav.infrastructure.config.impl.JavaConfig;
import org.evgenysav.infrastructure.core.Cache;
import org.evgenysav.infrastructure.core.Context;
import org.evgenysav.infrastructure.core.ObjectFactory;

import java.util.Map;

public class ApplicationContext implements Context {

    private final Config config;
    private final Cache cache;
    private final ObjectFactory factory;

    public ApplicationContext(String packageToScan, Map<Class<?>, Class<?>> interfaceToImplementation) {
        config = new JavaConfig(new ScannerImpl(packageToScan), interfaceToImplementation);
        cache = new CacheImpl();
        cache.put(Context.class, this);
        factory = new ObjectFactoryImpl(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getObject(Class<T> type) {
        if (cache.contains(type)){
            return cache.get(type);
        }

        Object object;
        if (type.isInterface()) {
            Class<?> realisation = config.getImplementation(type);
            object = factory.createObject(realisation);
        } else {
            object = factory.createObject(type);
        }

        cache.put(type, (T) object);
        return (T) object;
    }

    @Override
    public Config getConfig() {
        return config;
    }
}
