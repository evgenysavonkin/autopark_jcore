package org.evgenysav.infrastructure.dto.service.impl;

import org.evgenysav.infrastructure.core.annotations.Autowired;
import org.evgenysav.infrastructure.dto.EntityManager;
import org.evgenysav.infrastructure.dto.entity.OrderEntries;
import org.evgenysav.infrastructure.dto.service.OrderEntriesService;

import java.util.List;
import java.util.Optional;

public class OrderEntriesServiceImpl implements OrderEntriesService {

    @Autowired
    private EntityManager entityManager;

    @Override
    public OrderEntries get(Long id) {
        Optional<OrderEntries> entriesOptional = entityManager.get(id, OrderEntries.class);
        return entriesOptional.orElseThrow(() -> new RuntimeException("Order entry with id = " + id + " wasn't found"));
    }

    @Override
    public List<OrderEntries> getAll() {
        return entityManager.getAll(OrderEntries.class);
    }

    @Override
    public Long save(OrderEntries orderEntry) {
        return entityManager.save(orderEntry);
    }
}
