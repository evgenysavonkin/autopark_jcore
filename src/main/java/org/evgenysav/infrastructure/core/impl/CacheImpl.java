package org.evgenysav.infrastructure.core.impl;

import org.evgenysav.infrastructure.core.Cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class CacheImpl implements Cache {

    /**
     * A key represents name of class
     * <p>
     * A value represent instance of class
     */
    private Map<String, Object> cache;

    public CacheImpl() {
        this.cache = new HashMap<>();
    }

    /**
     * @param clazz
     * @return boolean if there is name of class of param clazz in cache
     */
    @Override
    public boolean contains(Class<?> clazz) {
        return Stream.of(cache)
                .anyMatch(k -> k.containsKey(clazz.getSimpleName()));
    }

    /**
     * @param clazz
     * @return T if name of clazz contains in cache
     * @throws NullPointerException if an argument is null
     */

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Class<T> clazz) {
        if (clazz == null){
            throw new NullPointerException("clazz is null");
        }

        if (contains(clazz)) {
            Optional<Object> optional = cache.entrySet().stream()
                    .filter(entry -> entry.getKey().equals(clazz.getSimpleName()))
                    .map(Map.Entry::getValue)
                    .findFirst();

            if (optional.isPresent()){
                return (T) optional.get();
            }
        }

        return null;
    }

    @Override
    public <T> void put(Class<T> target, T value) {
        cache.put(target.getSimpleName(), value);
    }
}
