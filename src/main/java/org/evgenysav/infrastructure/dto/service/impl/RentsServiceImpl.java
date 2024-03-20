package org.evgenysav.infrastructure.dto.service.impl;

import org.evgenysav.classes.Rent;
import org.evgenysav.classes_.VehicleCollection;
import org.evgenysav.infrastructure.core.annotations.Autowired;
import org.evgenysav.infrastructure.core.annotations.InitMethod;
import org.evgenysav.infrastructure.dto.EntityManager;
import org.evgenysav.infrastructure.dto.entity.Rents;
import org.evgenysav.infrastructure.dto.service.RentsService;

import java.util.List;
import java.util.Optional;

public class RentsServiceImpl implements RentsService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private VehicleCollection vehicleCollection;

    @InitMethod
    public void init() {
        List<Rent> rents = vehicleCollection.getRents();
        List<Rents> targetList = rents.stream()
                .map((Rent rent) -> {
                    Rents toRent = new Rents();
                    toRent.setVehicleId((long) rent.getId());
                    toRent.setRentDate(rent.getRentalDate());
                    toRent.setRentalCost(rent.getRentCost());
                    return toRent;
                }).toList();

        targetList.forEach(entityManager::save);
    }

    @Override
    public Rents get(Long id) {
        Optional<Rents> optional = entityManager.get(id, Rents.class);
        return optional.orElse(new Rents());
    }

    @Override
    public List<Rents> getAll() {
        return entityManager.getAll(Rents.class);
    }

    @Override
    public Long save(Rents rent) {
        return entityManager.save(rent);
    }
}
