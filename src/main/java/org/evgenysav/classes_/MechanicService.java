package org.evgenysav.classes_;

import org.evgenysav.classes.Fixer;
import org.evgenysav.classes.Vehicle;
import org.evgenysav.infrastructure.core.annotations.Autowired;
import org.evgenysav.util.FileActions;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MechanicService implements Fixer {

    private final Random random = new Random();
    private static final String[] DETAILS = new String[]{"Фильтр", "Фланец", "Втулка", "Вал", "Ось", "Свеча", "Масло", "ГРМ", "ШРУС"};
    private static List<String> linesFromOrdersFile = new ArrayList<>();
    private static final String FILENAME = "orders";
    private static final String FILENAME_PATH = "csv/" + FILENAME + ".csv";
    private static final StringBuilder stringBuilder = new StringBuilder();
    @Autowired
    private ParserBreakingFromFile parser;


    @Override
    public Map<String, Integer> detectBreaking(Vehicle vehicle) {
        int numberOfDetails = random.nextInt(DETAILS.length);
        if (numberOfDetails == 0) {
            return Collections.emptyMap();
        }

        Map<String, Integer> mapOfBreakDowns = new HashMap<>();
        Set<Integer> takenIndexes = new HashSet<>();
        while (numberOfDetails != 0) {
            int randomKey = random.nextInt(DETAILS.length);
            if (takenIndexes.contains(randomKey)) {
                while (takenIndexes.contains(randomKey)) {
                    randomKey = random.nextInt(DETAILS.length);
                }
            }

            takenIndexes.add(randomKey);
            mapOfBreakDowns.put(DETAILS[randomKey], random.nextInt(10) + 1);
            numberOfDetails--;
        }

        long defectCount = mapOfBreakDowns.values().stream()
                .mapToInt(e -> e.intValue())
                .sum();
        vehicle.setDefectCount(defectCount);

        parser.printMapToFIle(vehicle, mapOfBreakDowns);
        return mapOfBreakDowns;
    }

    @Override
    public void repair(Vehicle vehicle) {
        int vehicleId = vehicle.getVehicleId();
        try {
            linesFromOrdersFile = FileActions.getLinesFromFile(FILENAME);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        Iterator<String> iterator = linesFromOrdersFile.iterator();
        while (iterator.hasNext()) {
            String line = iterator.next();
            String[] strings = parser.getStringsFromLine(line);
            if (strings[0].equals(String.valueOf(vehicleId))) {
                iterator.remove();
            }
        }

        System.out.println(vehicle.getModelName() + " was repaired");


        try (FileWriter fileWriter = new FileWriter(FILENAME_PATH)) {
            for (String s : linesFromOrdersFile) {
                fileWriter.write(s + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public int findVehicleIdByStringFromFile(String line) {
        try {
            linesFromOrdersFile = FileActions.getLinesFromFile("orders");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        if (linesFromOrdersFile != null) {
            for (String s : linesFromOrdersFile) {
                if (s.contains(line)) {
                    String[] strings = parser.getStringsFromLine(s);
                    return Integer.parseInt(strings[0]);
                }
            }
        }

        return -1;
    }

    @Override
    public boolean isBroken(Vehicle vehicle) {
        try {
            linesFromOrdersFile = FileActions.getLinesFromFile(FILENAME);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        if (linesFromOrdersFile != null) {
            for (String line : linesFromOrdersFile) {
                String[] strings = parser.getStringsFromLine(line);
                String vehicleId = String.valueOf(vehicle.getVehicleId());
                if (strings[0].equals(vehicleId)) {
                    return true;
                }
            }
        }

        return false;
    }
}
