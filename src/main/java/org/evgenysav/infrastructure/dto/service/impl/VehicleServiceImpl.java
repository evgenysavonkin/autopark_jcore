package org.evgenysav.infrastructure.dto.service.impl;

import org.evgenysav.classes.*;
import org.evgenysav.classes_.VehicleCollection;
import org.evgenysav.infrastructure.core.Context;
import org.evgenysav.infrastructure.core.annotations.Autowired;
import org.evgenysav.infrastructure.core.annotations.InitMethod;
import org.evgenysav.infrastructure.dto.EntityManager;
import org.evgenysav.infrastructure.dto.entity.*;
import org.evgenysav.infrastructure.dto.service.ColorService;
import org.evgenysav.infrastructure.dto.service.RentsService;
import org.evgenysav.infrastructure.dto.service.VehicleService;

import java.util.List;
import java.util.Optional;

public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private VehicleCollection vehicleCollection;

    @Autowired
    private RentsService rentsService;

    @Autowired
    private Context context;

    @InitMethod
    public void init() {
        List<Vehicle> vehicleList = vehicleCollection.getVehicles();
        List<Vehicles> targetList = vehicleList.stream()
                .map((Vehicle vehicle) -> {
                    Vehicles vehicleTarget = new Vehicles();
                    vehicleTarget.setVehicleId((long) vehicle.getVehicleId());
                    vehicleTarget.setVehicleTypeId((long) vehicle.getVehicleTypeId());
                    vehicleTarget.setModelName(vehicle.getModelName());
                    vehicleTarget.setRegistrationNumber(vehicle.getRegistrationNumber());
                    vehicleTarget.setWeight((long) vehicle.getWeight());
                    vehicleTarget.setManufactureYear((long) vehicle.getManufactureYear());
                    vehicleTarget.setMileage((long) vehicle.getMileage());
                    vehicleTarget.setTankVolume((long) vehicle.getTankVolume());
                    return vehicleTarget;
                }).toList();


        targetList.forEach(entityManager::save);
    }

    @Override
    public Vehicles get(Long id) {
        Optional<Vehicles> vehicleOptional = entityManager.get(id, Vehicles.class);
        if (vehicleOptional.isEmpty()) {
            return new Vehicles();
        }

        Vehicles vehicle = vehicleOptional.get();
        setFieldsToVehicle(vehicle);

        return vehicle;
    }

    @Override
    public List<Vehicles> getAll() {
        List<Vehicles> allVehicles = entityManager.getAll(Vehicles.class);
        allVehicles.forEach(this::setFieldsToVehicle);
        return allVehicles;
    }

    @Override
    public Long save(Vehicles vehicles) {
        return entityManager.save(vehicles);
    }

    private void setFieldsToVehicle(Vehicles vehicle) {
        setRentsToVehicle(vehicle);
        List<CombustionStartable> combustionEngines = entityManager.getAll(CombustionStartable.class);
        List<ElectricalStartable> electricalEngines = entityManager.getAll(ElectricalStartable.class);
        for (CombustionStartable engine : combustionEngines) {
            if (engine.getVehicleId().equals(vehicle.getVehicleId())) {
                setCombustionEngineToVehicle(engine, vehicle);
                break;
            }
        }

        for (ElectricalStartable engine : electricalEngines) {
            if (engine.getVehicleId().equals(vehicle.getVehicleId())) {
                setElectricalEngineToVehicle(engine, vehicle);
                break;
            }
        }

        List<Types> vehicleTypes = entityManager.getAll(Types.class);
        for (Types type : vehicleTypes) {
            if (type.getTypeId().equals(vehicle.getVehicleTypeId())) {
                setVehicleType(type, vehicle);
                break;
            }
        }

        ColorService colorService = context.getObject(ColorService.class);

        List<Color_> colors = colorService.getAll();
        colors.stream()
                .filter(color -> color.getVehicleId().equals(vehicle.getVehicleId()))
                .forEach(color -> {
                    String colorStr = color.getColor();
                    Color.valueOf(colorStr.toUpperCase());
                    vehicle.setColor(Color.valueOf(colorStr.toUpperCase()));
                });
    }

    private void setVehicleType(Types vehicleTypeToConvert, Vehicles vehicle) {
        VehicleType vehicleType = new VehicleType();
        vehicleType.setId(Math.toIntExact(vehicleTypeToConvert.getId()));
        vehicleType.setTypeName(vehicleTypeToConvert.getName());
        vehicleType.setTaxCoefficient(vehicleTypeToConvert.getTaxCoefficient());
        vehicle.setType(vehicleType);
    }

    private void setElectricalEngineToVehicle(ElectricalStartable electricalStartable, Vehicles vehicle) {
        ElectricalEngine engine = new ElectricalEngine();
        engine.setTypeOfEngine("Electrical");
        engine.setBatterySize(electricalStartable.getBatterySize());
        engine.setElectricityConsumption(electricalStartable.getElectricityConsumptionPer100());
        engine.setTaxCoefficient(0.1);
        vehicle.setStartable(engine);
    }

    private void setCombustionEngineToVehicle(CombustionStartable combustionStartable, Vehicles vehicle) {
        String engineType = combustionStartable.getEngineType();
        if (engineType.equals("Diesel")) {
            DieselEngine dieselEngine = new DieselEngine();
            dieselEngine.setTypeOfEngine("Diesel");
            dieselEngine.setEngineCapacity(combustionStartable.getEngineCapacity());
            dieselEngine.setFuelTankCapacity(combustionStartable.getFuelTankCapacity());
            dieselEngine.setFuelConsumptionPer100(combustionStartable.getFuelConsumptionPer100());
            dieselEngine.setTaxCoefficient(combustionStartable.getTaxCoefficient());
            vehicle.setStartable(dieselEngine);
        }
        if (engineType.equals("Gasoline")) {
            GasolineEngine gasolineEngine = new GasolineEngine();
            gasolineEngine.setTypeOfEngine("Gasoline");
            gasolineEngine.setEngineCapacity(combustionStartable.getEngineCapacity());
            gasolineEngine.setFuelTankCapacity(combustionStartable.getFuelTankCapacity());
            gasolineEngine.setFuelConsumptionPer100(combustionStartable.getFuelConsumptionPer100());
            gasolineEngine.setTaxCoefficient(gasolineEngine.getTaxCoefficient());
            vehicle.setStartable(gasolineEngine);
        }
    }

    private void setRentsToVehicle(Vehicles vehicle) {
        List<Rents> allRents = rentsService.getAll();
        List<Rents> vehicleRents = allRents.stream()
                .filter(r -> r.getVehicleId().equals(vehicle.getVehicleId()))
                .toList();

        vehicle.setMachineOrders(vehicleRents);
    }
}
