package org.evgenysav.infrastructure.core.impl;

import lombok.SneakyThrows;
import org.evgenysav.infrastructure.configurators.ObjectConfigurator;
import org.evgenysav.infrastructure.configurators.ProxyConfigurator;
import org.evgenysav.infrastructure.core.Context;
import org.evgenysav.infrastructure.core.ObjectFactory;
import org.evgenysav.infrastructure.core.Scanner;
import org.evgenysav.infrastructure.core.annotations.InitMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ObjectFactoryImpl implements ObjectFactory {

    private final Context context;
    private final List<ObjectConfigurator> objectConfigurators = new ArrayList<>();
    private final List<ProxyConfigurator> proxyConfigurators = new ArrayList<>();

    @SneakyThrows
    public ObjectFactoryImpl(Context context) {
        this.context = context;
        Scanner scanner = context.getConfig().getScanner();
        Set<Class<? extends ObjectConfigurator>> subTypesOfConfigurators =
                scanner.getSubTypesOf(ObjectConfigurator.class);
        for (var configurator : subTypesOfConfigurators) {
            objectConfigurators.add(configurator.newInstance());
        }

        Set<Class<? extends ProxyConfigurator>> subTypesOfProxyConfigurators =
                scanner.getSubTypesOf(ProxyConfigurator.class);

        for (Class<? extends ProxyConfigurator> implementation : subTypesOfProxyConfigurators) {
            proxyConfigurators.add(implementation.newInstance());
        }
    }

    @SneakyThrows
    @Override
    public <T> T createObject(Class<T> implementation) {
        T object = create(implementation);
        configure(object);
        initialize(implementation, object);
        object = makeProxy(implementation, object);
        return object;
    }

    private <T> T makeProxy(Class<T> implClass, T object) {
        for (ProxyConfigurator proxy : proxyConfigurators) {
            object = proxy.makeProxy(object, implClass, context);
        }

        return object;
    }

    private <T> T create(Class<T> implementation) throws Exception {
        return implementation.newInstance();
    }

    private <T> void configure(T object) {
        for (var configurator : objectConfigurators) {
            configurator.configure(object, context);
        }
    }

    @SneakyThrows
    private <T> void initialize(Class<T> implementation, T object) {
        for (Method method : implementation.getDeclaredMethods()) {
            if (method.isAnnotationPresent(InitMethod.class)) {
                method.setAccessible(true);
                method.invoke(object);
            }
        }
    }
}
