package org.evgenysav.infrastructure.dto.service;

import org.evgenysav.infrastructure.dto.entity.Types;

import java.util.List;

public interface TypesService {
    Types get(Long id);

    List<Types> getAll();

    Long save(Types type);
}
