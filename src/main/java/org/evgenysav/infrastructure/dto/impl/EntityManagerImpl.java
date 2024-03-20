package org.evgenysav.infrastructure.dto.impl;

import lombok.NoArgsConstructor;
import org.evgenysav.infrastructure.core.Context;
import org.evgenysav.infrastructure.core.annotations.Autowired;
import org.evgenysav.infrastructure.dto.ConnectionFactory;
import org.evgenysav.infrastructure.dto.DataBaseService;
import org.evgenysav.infrastructure.dto.EntityManager;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class EntityManagerImpl implements EntityManager {

    @Autowired
    private ConnectionFactory connection;

    @Autowired
    private DataBaseService dataBaseService;

    @Autowired
    private Context context;


    @Override
    public <T> Optional<T> get(Long id, Class<T> clazz) {
        return dataBaseService.get(id, clazz);
    }

    @Override
    public Long save(Object object) {
        return dataBaseService.save(object);
    }

    @Override
    public <T> List<T> getAll(Class<T> clazz) {
        return dataBaseService.getAll(clazz);
    }
}
