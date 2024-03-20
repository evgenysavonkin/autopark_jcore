package org.evgenysav.infrastructure.dto.service.impl;

import lombok.SneakyThrows;
import org.evgenysav.infrastructure.core.annotations.Autowired;
import org.evgenysav.infrastructure.core.annotations.InitMethod;
import org.evgenysav.infrastructure.dto.EntityManager;
import org.evgenysav.infrastructure.dto.entity.ElectricalStartable;
import org.evgenysav.infrastructure.dto.service.ElectricalEngineService;
import org.evgenysav.util.FileActions;

import java.util.List;
import java.util.Optional;

public class ElectricalEngineServiceImpl implements ElectricalEngineService {

    @Autowired
    private EntityManager entityManager;

    @Override
    public ElectricalStartable get(Long id) {
        Optional<ElectricalStartable> engineOptional = entityManager.get(id, ElectricalStartable.class);
        return engineOptional.orElseThrow(() -> new RuntimeException("Engine with id = " + id + " wasn't found"));
    }

    @Override
    public List<ElectricalStartable> getAll() {
        return entityManager.getAll(ElectricalStartable.class);
    }

    @Override
    public Long save(ElectricalStartable engine) {
        return entityManager.save(engine);
    }

    @SneakyThrows
    @InitMethod
    public void init() {
        List<Object> engines = FileActions.getEngineListFromFile("vehicles");
        for (Object o : engines) {
            if (o instanceof ElectricalStartable electricalStartable) {
                save(electricalStartable);
            }
        }
    }
}
