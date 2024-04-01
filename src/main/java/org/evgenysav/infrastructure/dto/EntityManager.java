package org.evgenysav.infrastructure.dto;

import java.util.List;
import java.util.Optional;

public interface EntityManager {
    <T> Optional<T> get(Long id, Class<T> clazz);

    Long save(Object object);

    <T> List<T> getAll(Class<T> clazz);

    void remove(Object obj, String fieldName, Long fieldId);
}
