package org.evgenysav.infrastructure.dto;

import java.util.List;
import java.util.Optional;

public interface DataBaseService {
    Long save(Object obj);

    <T> Optional<T> get(Long id, Class<T> clazz);

    <T> List<T> getAll(Class<T> clazz);

    void remove(Object obj, String fieldName, Long fieldId);
}
