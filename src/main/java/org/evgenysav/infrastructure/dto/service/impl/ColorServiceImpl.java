package org.evgenysav.infrastructure.dto.service.impl;

import lombok.SneakyThrows;
import org.evgenysav.infrastructure.core.annotations.Autowired;
import org.evgenysav.infrastructure.core.annotations.InitMethod;
import org.evgenysav.infrastructure.dto.EntityManager;
import org.evgenysav.infrastructure.dto.entity.Color_;
import org.evgenysav.infrastructure.dto.service.ColorService;
import org.evgenysav.util.FileActions;

import java.util.List;
import java.util.Optional;

public class ColorServiceImpl implements ColorService {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Color_ get(Long id) {
        Optional<Color_> colorOptional = entityManager.get(id, Color_.class);
        return colorOptional.orElseThrow(() -> new RuntimeException("Color with id = " + id + " wasn't found"));
    }

    @Override
    public List<Color_> getAll() {
        return entityManager.getAll(Color_.class);
    }

    @Override
    public Long save(Color_ color) {
        return entityManager.save(color);
    }

    @SneakyThrows
    @InitMethod
    public void init() {
        List<Color_> colors = FileActions.getColorsList("vehicles");
        colors.forEach(this::save);
    }
}
