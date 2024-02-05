package org.evgenysav;

public class TechnicalSpecialist {

    private TechnicalSpecialist() {

    }

    private static final int LOWER_LIMIT_MANUFACTURE_YEAR = 1886;

    public static boolean validateManufactureYear(int year) {
        return year > LOWER_LIMIT_MANUFACTURE_YEAR && String.valueOf(year).length() == 4;
    }

    public static boolean validateMileage(int mileage) {
        return mileage >= 0;
    }

    public static boolean validateWeight(int weight) {
        return weight >= 0;
    }

    public static boolean validateColor(Color color) {
        return color != null;
    }

    public static boolean validateVehicleType(VehicleType vehicleType) {
        if (vehicleType.getTypeName() != null && vehicleType.getTaxCoefficient() >= 0) {
            return !vehicleType.getTypeName().isEmpty();
        }

        return false;
    }

    public static boolean validateRegistrationNumber(String number) {
        if (number != null) {
            String[] strings = number.split(" ");
            if (strings.length != 2) {
                return false;
            }

            try {
                int year = Integer.parseInt(strings[0]);
                if (strings[0].length() != 4) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }

            String[] lastPartFromNumber = strings[1].split("-");
            if (lastPartFromNumber[0].matches("^[A-Z]*$") && lastPartFromNumber[0].length() == 2) {
                if (lastPartFromNumber[1].length() > 1) {
                    return false;
                }

                try {
                    Integer.parseInt(lastPartFromNumber[1]);
                } catch (NumberFormatException e) {
                    return false;
                }

                return true;
            }
        }

        return false;
    }

    public static boolean validateModelName(String name) {
        if (name != null) {
            return !name.isEmpty();
        }

        return false;
    }

}
