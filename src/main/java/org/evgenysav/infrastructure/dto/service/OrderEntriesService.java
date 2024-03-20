package org.evgenysav.infrastructure.dto.service;

import org.evgenysav.infrastructure.dto.entity.OrderEntries;

import java.util.List;

public interface OrderEntriesService {
    OrderEntries get(Long id);

    List<OrderEntries> getAll();

    Long save(OrderEntries rent);
}
