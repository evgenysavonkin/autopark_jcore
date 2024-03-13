package org.evgenysav.infrastructure.dto.impl;

import org.evgenysav.infrastructure.core.Context;
import org.evgenysav.infrastructure.core.annotations.Autowired;
import org.evgenysav.infrastructure.dto.ConnectionFactory;
import org.evgenysav.infrastructure.dto.EntityManager;
import org.evgenysav.infrastructure.dto.PostgreDataBase;

import java.util.List;
import java.util.Optional;

public class EntityManagerImpl implements EntityManager {

    @Autowired
    private ConnectionFactory connection;

    @Autowired
    private PostgreDataBase dataBaseService;

    @Autowired
    private Context context;


    @Override
    public <T> Optional<T> get(Long id, Class<T> clazz) {
        return Optional.empty();
    }

    @Override
    public Long save(Object object) {
        return null;
    }

    @Override
    public <T> List<T> getAll(Class<T> clazz) {
        return null;
    }
}
