package org.evgenysav.infrastructure.dto.service;

import org.evgenysav.infrastructure.dto.entity.Rents;
import org.evgenysav.infrastructure.dto.entity.Vehicles;

import java.util.List;

public interface VehicleService {
    Vehicles get(Long id);

    List<Vehicles> getAll();

    Long save(Vehicles rent);
}
