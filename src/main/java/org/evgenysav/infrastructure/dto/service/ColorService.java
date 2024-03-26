package org.evgenysav.infrastructure.dto.service;

import org.evgenysav.infrastructure.dto.entity.Color_;

import java.util.List;

public interface ColorService {
    Color_ get(Long id);

    List<Color_> getAll();

    Long save(Color_ color);
}
