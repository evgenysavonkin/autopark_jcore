package org.evgenysav;

import org.evgenysav.exceptions.NotVehicleException;

public class TechnicalSpecialist {

    private TechnicalSpecialist() {
    }

    private static final int LOWER_LIMIT_MANUFACTURE_YEAR = 1886;

    public static boolean validateManufactureYear(int year) throws NotVehicleException {
        if (year > LOWER_LIMIT_MANUFACTURE_YEAR && String.valueOf(year).length() == 4) {
            return true;
        }

        throw new NotVehicleException("Invalid manufactureYear: year = " + year);
    }

    public static boolean validateMileage(int mileage) throws NotVehicleException {
        if (mileage >= 0) {
            return true;
        }

        throw new NotVehicleException("Invalid mileage: mileage = " + mileage);
    }

    public static boolean validateWeight(int weight) throws NotVehicleException {
        if (weight >= 0) {
            return true;
        }

        throw new NotVehicleException("Invalid weight: weight = " + weight);
    }

    public static boolean validateColor(Color color) throws NotVehicleException {
        if (color != null) {
            return true;
        }

        throw new NotVehicleException("Invalid color: color is null");
    }

    public static boolean validateVehicleType(VehicleType vehicleType) throws NotVehicleException {
        if (vehicleType.getTypeName() == null || vehicleType.getTypeName().isEmpty()) {
            throw new NotVehicleException("Invalid typeName: typeName is null or empty");
        }

        if (vehicleType.getTaxCoefficient() < 0) {
            throw new NotVehicleException("Invalid taxCoefficient: taxCoefficient = " + vehicleType.getTaxCoefficient());
        }

        return true;
    }

    public static boolean validateRegistrationNumber(String number) throws NotVehicleException {
        if (number == null) {
            throw new NotVehicleException("Invalid registrationNumber: registrationNumber is null");
        }
        String[] strings = number.split(" ");
        if (strings.length != 2) {
            throw new NotVehicleException("Invalid registrationNumber: expected one space in a registration number. " +
                    " Actual was: " + number);
        }

        try {
            Integer.parseInt(strings[0]);
            if (strings[0].length() != 4) {
                throw new NotVehicleException("Invalid registrationNumber: expected 4 symbols in the first part of a registration number. " +
                        "Actual was: " + strings[0]);
            }
        } catch (NumberFormatException e) {
            throw new NotVehicleException("Invalid registrationNumber: expected only digits in the first part of a registration number. " +
                    "Actual was: " + strings[0]);
        }

        String[] lastPartFromNumber = strings[1].split("-");
        if (lastPartFromNumber[0].matches("^[A-Z]*$") && lastPartFromNumber[0].length() == 2) {
            if (lastPartFromNumber[1].length() > 1) {
                throw new NotVehicleException("Invalid registrationNumber: expected one symbol in the end of registration number." +
                        " Actual was: " + lastPartFromNumber[1]);
            }
            try {
                Integer.parseInt(lastPartFromNumber[1]);
            } catch (NumberFormatException e) {
                throw new NotVehicleException("Invalid registrationNumber: " +
                        "expected a digit in the last symbol of registration number. Actual was: " + lastPartFromNumber[1]);
            }

            return true;
        }

        throw new NotVehicleException("Invalid registrationNumber: incorrect registration number " + number);
    }

    public static boolean validateModelName(String name) throws NotVehicleException {
        if (name == null || name.isEmpty()) {
            throw new NotVehicleException("Invalid modelName: modelName is null or empty");
        }

        return true;
    }

    public static boolean validateGasolineEngine(GasolineEngine engine) throws NotVehicleException {
        return validateCombustionEngine(engine);
    }

    public static boolean validateElectricalEngine(ElectricalEngine engine) throws NotVehicleException {
        if (engine.getTypeOfEngine() == null || engine.getTypeOfEngine().isEmpty()) {
            throw new NotVehicleException("Invalid electicalEngine: engine is null or empty");
        }
        if (engine.getTaxCoefficient() < 0) {
            throw new NotVehicleException("Invalid electricalEngine taxCoefficient: taxCoefficient = " + engine.getTaxCoefficient());
        }
        if (engine.getBatterySize() < 0) {
            throw new NotVehicleException("Invalid electricalEngine batterySize: batterySize = " + engine.getBatterySize());
        }
        if (engine.getElectricityConsumption() < 0) {
            throw new NotVehicleException("Invalid electricalEngine electricityConsumption: electricityConsumption = " + engine.getElectricityConsumption());
        }

        return true;
    }

    public static boolean validateDieselEngine(DieselEngine engine) throws NotVehicleException {
        return validateCombustionEngine(engine);
    }

    private static boolean validateCombustionEngine(CombustionEngine engine) throws NotVehicleException {
        if (engine.getTypeOfEngine() == null || engine.getTypeOfEngine().isEmpty()) {
            throw new NotVehicleException("Invalid combustionEngine: engine is null or empty");
        }
        if (engine.getTaxCoefficient() < 0) {
            throw new NotVehicleException("Invalid combustionEngine taxCoefficient: taxCoefficient = " + engine.getTaxCoefficient());
        }
        if (engine.getFuelTankCapacity() < 0) {
            throw new NotVehicleException("Invalid combustionEngine fuelTankCapacity: fuelTankCapacity = " + engine.getFuelTankCapacity());
        }
        if (engine.getFuelConsumptionPer100() < 0) {
            throw new NotVehicleException("Invalid combustionEngine fuelConsumptionPer100: fuelConsumptionPer100 = " + engine.getFuelConsumptionPer100());
        }

        return true;
    }
}
