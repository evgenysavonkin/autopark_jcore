package org.evgenysav.infrastructure.dto.service;

import org.evgenysav.infrastructure.dto.entity.Orders;

import java.util.List;

public interface OrdersService {
    Orders get(Long id);

    List<Orders> getAll();

    Long save(Orders rent);

    void remove(Object obj, String fieldName, Long fieldId);
}
