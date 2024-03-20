package org.evgenysav.infrastructure.dto.service;

import org.evgenysav.infrastructure.dto.entity.Orders;
import org.evgenysav.infrastructure.dto.entity.Rents;

import java.util.List;

public interface OrdersService {
    Orders get(Long id);

    List<Orders> getAll();

    Long save(Orders rent);
}
