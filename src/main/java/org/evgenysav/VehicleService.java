package org.evgenysav;

import static org.evgenysav.TechnicalSpecialist.*;

public class VehicleService {
    public static Vehicle getVehicleWithNullValues(VehicleType type, String modelName, String registrationNumber,
                                                   int weight, int manufactureYear, int mileage, Color color, int tankVolume) {
        Vehicle vehicle = new Vehicle();
        if (!validateVehicleType(type)) {
            vehicle.setType(null);
        } else {
            vehicle.setType(type);
        }
        if (!validateModelName(modelName)) {
            vehicle.setModelName("");
        } else {
            vehicle.setModelName(modelName);
        }
        if (!validateRegistrationNumber(registrationNumber)) {
            vehicle.setRegistrationNumber("");
        } else {
            vehicle.setRegistrationNumber(registrationNumber);
        }
        if (!validateWeight(weight)) {
            vehicle.setWeight(0);
        } else {
            vehicle.setWeight(weight);
        }
        if (!validateManufactureYear(manufactureYear)) {
            vehicle.setManufactureYear(0);
        } else {
            vehicle.setManufactureYear(manufactureYear);
        }
        if (!validateMileage(mileage)) {
            vehicle.setMileage(0);
        } else {
            vehicle.setMileage(mileage);
        }
        if (!validateColor(color)) {
            vehicle.setColor(null);
        } else {
            vehicle.setColor(color);
        }

        return vehicle;
    }
}
