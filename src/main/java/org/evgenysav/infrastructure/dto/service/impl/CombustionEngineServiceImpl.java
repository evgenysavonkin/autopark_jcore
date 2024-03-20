package org.evgenysav.infrastructure.dto.service.impl;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.evgenysav.infrastructure.core.annotations.Autowired;
import org.evgenysav.infrastructure.core.annotations.InitMethod;
import org.evgenysav.infrastructure.dto.EntityManager;
import org.evgenysav.infrastructure.dto.entity.CombustionStartable;
import org.evgenysav.infrastructure.dto.service.CombustionEngineService;
import org.evgenysav.util.FileActions;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class CombustionEngineServiceImpl implements CombustionEngineService {

    @Autowired
    private EntityManager entityManager;

    @Override
    public CombustionStartable get(Long id) {
        Optional<CombustionStartable> startableOptional = entityManager.get(id, CombustionStartable.class);
        return startableOptional.orElseThrow(() -> new RuntimeException("Engine with id = " + id + " wasn't found"));
    }

    @Override
    public List<CombustionStartable> getAll() {
        return entityManager.getAll(CombustionStartable.class);
    }

    @Override
    public Long save(CombustionStartable engine) {
        return entityManager.save(engine);
    }

    @SneakyThrows
    @InitMethod
    public void init() {
        List<Object> engines = FileActions.getEngineListFromFile("vehicles");
        for (Object o : engines) {
            if (o instanceof CombustionStartable combustionEngine) {
                save(combustionEngine);
            }
        }
    }
}
