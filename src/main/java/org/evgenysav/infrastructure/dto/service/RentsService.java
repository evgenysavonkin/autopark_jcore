package org.evgenysav.infrastructure.dto.service;

import org.evgenysav.infrastructure.dto.entity.Rents;
import org.evgenysav.infrastructure.dto.entity.Types;

import java.util.List;

public interface RentsService {
    Rents get(Long id);

    List<Rents> getAll();

    Long save(Rents rent);
}
