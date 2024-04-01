package org.evgenysav.infrastructure.dto.service.impl;

import org.evgenysav.infrastructure.core.annotations.Autowired;
import org.evgenysav.infrastructure.dto.EntityManager;
import org.evgenysav.infrastructure.dto.entity.Orders;
import org.evgenysav.infrastructure.dto.service.OrdersService;

import java.util.List;
import java.util.Optional;

public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Orders get(Long id) {
        Optional<Orders> orders = entityManager.get(id, Orders.class);
        return orders.orElseThrow(() -> new RuntimeException("Order with id = " + id + " wasn't found"));
    }

    @Override
    public List<Orders> getAll() {
        return entityManager.getAll(Orders.class);
    }

    @Override
    public Long save(Orders rent) {
        return entityManager.save(rent);
    }

    @Override
    public void remove(Object obj, String fieldName, Long fieldId) {
        entityManager.remove(obj, fieldName, fieldId);
    }
}
