package org.evgenysav.infrastructure.dto.service;

import org.evgenysav.infrastructure.dto.entity.ElectricalStartable;

import java.util.List;

public interface ElectricalEngineService {
    ElectricalStartable get(Long id);

    List<ElectricalStartable> getAll();

    Long save(ElectricalStartable engine);
}
