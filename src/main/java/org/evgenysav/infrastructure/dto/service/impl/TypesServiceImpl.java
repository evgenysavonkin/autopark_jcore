package org.evgenysav.infrastructure.dto.service.impl;

import org.evgenysav.classes.VehicleType;
import org.evgenysav.classes_.VehicleCollection;
import org.evgenysav.infrastructure.core.annotations.Autowired;
import org.evgenysav.infrastructure.core.annotations.InitMethod;
import org.evgenysav.infrastructure.dto.EntityManager;
import org.evgenysav.infrastructure.dto.entity.Types;
import org.evgenysav.infrastructure.dto.service.TypesService;

import java.util.List;
import java.util.Optional;

public class TypesServiceImpl implements TypesService {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private VehicleCollection vehicleCollection;

    @InitMethod
    public void init() {
        List<VehicleType> vehicleTypes = vehicleCollection.getVehicleTypes();
        List<Types> transformedToDto = vehicleTypes.stream()
                .map((VehicleType vehicleType) -> {
                    Types types = new Types();
                    types.setTypeId((long) vehicleType.getId());
                    types.setName(vehicleType.getTypeName());
                    types.setTaxCoefficient(vehicleType.getTaxCoefficient());
                    return types;
                }).toList();

        transformedToDto.forEach(entityManager::save);
    }

    public Types get(Long id) {
        Optional<Types> optionalType = entityManager.get(id, Types.class);
        return optionalType.orElse(new Types());
    }

    public List<Types> getAll() {
        return entityManager.getAll(Types.class);
    }

    public Long save(Types type) {
        return entityManager.save(type);
    }
}
