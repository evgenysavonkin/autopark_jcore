package org.evgenysav.infrastructure.dto.service;

import org.evgenysav.infrastructure.dto.entity.CombustionStartable;

import java.util.List;

public interface CombustionEngineService {
    CombustionStartable get(Long id);

    List<CombustionStartable> getAll();

    Long save(CombustionStartable engine);
}
