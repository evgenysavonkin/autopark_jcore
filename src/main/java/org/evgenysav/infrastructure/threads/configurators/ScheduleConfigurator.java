package org.evgenysav.infrastructure.threads.configurators;

import lombok.SneakyThrows;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.evgenysav.infrastructure.configurators.ProxyConfigurator;
import org.evgenysav.infrastructure.core.Context;
import org.evgenysav.infrastructure.threads.annotations.Schedule;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeoutException;

public class ScheduleConfigurator implements ProxyConfigurator {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T makeProxy(T object, Class<T> implementation, Context context) {
        Class<?> objectClass = object.getClass();

        for (Method method : objectClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Schedule.class)) {
                if (!method.getReturnType().equals(Void.TYPE)) {
                    throw new RuntimeException("@Schedule " + method.getName() + " needs to have return type void");
                }
                if (!isPublicMethod(method)) {
                    throw new RuntimeException("@Schedule " + method.getName() + " needs to have public access modifier");
                }
                return (T) Enhancer.create(implementation, (MethodInterceptor) this::invoke);
            }
        }

        return object;
    }

    @SneakyThrows
    private Object invoke(Object object, Method method, Object[] args, MethodProxy methodProxy) {
        Schedule scheduleSync = method.getAnnotation(Schedule.class);
        if (scheduleSync != null) {
            Thread thread = new Thread(() -> this.invoker(object, methodProxy, args, scheduleSync.timeout(), scheduleSync.delta()));
            thread.setDaemon(true);
            thread.start();
            return null;
        }

        return methodProxy.invokeSuper(object, args);
    }

    private void invoker(Object obj, MethodProxy method, Object[] args, int milliseconds, int delta) {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread invokeThread = new Thread(() -> {
                        ExecutorService executorService =
                                Executors.newSingleThreadExecutor(new ThreadFactory() {
                                    @Override
                                    public Thread newThread(Runnable r) {
                                        Thread fThread =
                                                Executors.defaultThreadFactory().newThread(r);
                                        fThread.setDaemon(true);
                                        return fThread;
                                    }
                                });

                        try {
                            executorService.submit(() -> {
                                try {
                                    return method.invokeSuper(obj, args);
                                } catch (Throwable throwable) {

                                }
                                return null;
                            }).get(milliseconds, TimeUnit.MILLISECONDS);
                        } catch (TimeoutException exception) {
                            executorService.shutdownNow();
                        } catch (Exception exception) {
                            executorService.shutdownNow();
                        }

                        executorService.shutdown();
                    });

                    invokeThread.setDaemon(true);
                    invokeThread.start();
                    Thread.sleep(delta);
                } catch (InterruptedException e) {

                }
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private boolean isPublicMethod(Method method) {
        int modifiers = method.getModifiers();
        return Modifier.isPublic(modifiers);
    }
}
