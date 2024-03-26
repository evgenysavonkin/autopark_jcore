package org.evgenysav.util;

import org.evgenysav.classes.CombustionEngine;
import org.evgenysav.classes.ElectricalEngine;
import org.evgenysav.classes.Startable;
import org.evgenysav.infrastructure.dto.classes_dto.RentDto;
import org.evgenysav.infrastructure.dto.classes_dto.VehicleDto;
import org.evgenysav.infrastructure.dto.classes_dto.VehicleTypeDto;
import org.evgenysav.infrastructure.dto.entity.Rents;
import org.evgenysav.infrastructure.dto.entity.Types;
import org.evgenysav.infrastructure.dto.entity.Vehicles;

import java.util.List;

public class DtoConverter {

    private DtoConverter() {
    }

    public static List<VehicleTypeDto> getVehiclesTypeDto(List<Types> typesFromDB) {
        return typesFromDB.stream()
                .map((Types typeFromDB) -> {
                    VehicleTypeDto vehicleTypeDto = new VehicleTypeDto();
                    vehicleTypeDto.setVehicleId(typeFromDB.getTypeId());
                    vehicleTypeDto.setName(typeFromDB.getName());
                    vehicleTypeDto.setTaxCoefficient(typeFromDB.getTaxCoefficient());
                    return vehicleTypeDto;
                }).toList();
    }

    public static List<VehicleDto> getVehiclesDto(List<Vehicles> vehicleListFromDB) {
        return vehicleListFromDB.stream()
                .map((Vehicles vehicleFromDB) -> {
                    VehicleDto vehicleDto = new VehicleDto();
                    vehicleDto.setTypeId(vehicleFromDB.getType().getId());
                    vehicleDto.setTypeName(vehicleFromDB.getType().getTypeName());
                    vehicleDto.setTaxCoefficient(vehicleFromDB.getType().getTaxCoefficient());
                    vehicleDto.setModelName(vehicleFromDB.getModelName());
                    vehicleDto.setManufactureYear(Math.toIntExact(vehicleFromDB.getManufactureYear()));
                    vehicleDto.setRegistrationNumber(vehicleFromDB.getRegistrationNumber());
                    vehicleDto.setWeight(vehicleFromDB.getWeight());
                    vehicleDto.setMileage(Math.toIntExact(vehicleFromDB.getMileage()));
                    vehicleDto.setColor(String.valueOf(vehicleFromDB.getColor()));
                    vehicleDto.setEngineName(vehicleFromDB.getStartable().getStartableName());
                    vehicleDto.setEngineTaxCoefficient(vehicleFromDB.getStartable().getTaxPerMonth());
                    vehicleDto.setId(Math.toIntExact(vehicleFromDB.getId()));
                    vehicleDto.setVehicleId(vehicleFromDB.getVehicleId());
                    Startable engine = vehicleFromDB.getStartable();
                    if (engine instanceof CombustionEngine combustionEngine) {
                        vehicleDto.setPer100km(combustionEngine.getFuelConsumptionPer100());
                    } else {
                        ElectricalEngine electricalEngine = (ElectricalEngine) engine;
                        vehicleDto.setPer100km(electricalEngine.getElectricityConsumption());
                    }
                    vehicleDto.setMaxKm(engine.getMaxKilometers());
                    vehicleDto.setTax(engine.getTaxPerMonth());
                    return vehicleDto;
                }).toList();
    }

    public static List<RentDto> getRentsDto(List<Rents> rentsFromDB) {
        return rentsFromDB.stream()
                .map((Rents rentFromDB) -> {
                    RentDto rentDto = new RentDto();
                    rentDto.setId(rentFromDB.getId());
                    rentDto.setVehicleId(rentFromDB.getVehicleId());
                    rentDto.setRentDate(rentFromDB.getRentDate());
                    rentDto.setRentalCost(rentFromDB.getRentalCost());
                    return rentDto;
                }).toList();
    }
}
