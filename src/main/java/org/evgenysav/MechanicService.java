package org.evgenysav;

import org.evgenysav.util.FileActions;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class MechanicService implements Fixer {

    private static final String[] DETAILS = new String[]{"Фильтр", "Фланец", "Втулка", "Вал", "Ось", "Свеча", "Масло", "ГРМ", "ШРУС"};
    private static final String FILENAME = "orders";
    private static final String FILENAME_PATH = "csv/" + FILENAME + ".csv";
    private static final Random random = new Random();
    private static final StringBuilder stringBuilder = new StringBuilder();

    @Override
    public Map<String, Integer> detectBreaking(Vehicle vehicle) {
        int numberOfDetails = random.nextInt(DETAILS.length);
        if (numberOfDetails == 0) {
            return Collections.emptyMap();
        }

        Map<String, Integer> map = new HashMap<>();
        Set<Integer> takenIndexes = new HashSet<>();
        while (numberOfDetails != 0) {
            int randomKey = random.nextInt(DETAILS.length);
            if (takenIndexes.contains(randomKey)) {
                while (takenIndexes.contains(randomKey)) {
                    randomKey = random.nextInt(DETAILS.length);
                }
            }

            takenIndexes.add(randomKey);
            map.put(DETAILS[randomKey], random.nextInt(10) + 1);
            numberOfDetails--;
        }

        printMapToFIle(vehicle, map);
        return map;
    }

    @Override
    public void repair(Vehicle vehicle) {
        int vehicleId = vehicle.getVehicleId();
        List<String> linesFromFile = null;
        try {
            linesFromFile = FileActions.getLinesFromFile(FILENAME);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        Iterator<String> iterator = linesFromFile.iterator();
        while (iterator.hasNext()) {
            String line = iterator.next();
            String[] strings = getStringsFromLine(line);
            if (strings[0].equals(String.valueOf(vehicleId))) {
                iterator.remove();
            }
        }

        try (FileWriter fileWriter = new FileWriter(FILENAME_PATH)) {
            for (String s : linesFromFile) {
                fileWriter.write(s + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public int findVehicleIdByStringFromFile(String line) {
        List<String> linesFromFile = null;
        try {
            linesFromFile = FileActions.getLinesFromFile("orders");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        if (linesFromFile != null) {
            for (String s : linesFromFile) {
                if (s.contains(line)) {
                    String[] strings = getStringsFromLine(s);
                    return Integer.parseInt(strings[0]);
                }
            }
        }

        return -1;
    }

    @Override
    public boolean isBroken(Vehicle vehicle) {
        List<String> linesFromFile;
        try {
            linesFromFile = FileActions.getLinesFromFile(FILENAME);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        if (linesFromFile != null) {
            for (String line : linesFromFile) {
                String[] strings = getStringsFromLine(line);
                String vehicleId = String.valueOf(vehicle.getVehicleId());
                if (strings[0].equals(vehicleId)) {
                    return true;
                }
            }
        }

        return false;
    }

    private void printMapToFIle(Vehicle vehicle, Map<String, Integer> map) {
        stringBuilder.setLength(0);
        stringBuilder.append(vehicle.getVehicleId());
        for (var entry : map.entrySet()) {
            stringBuilder.append(",").append(entry.getKey()).append(",").append(entry.getValue());
        }
        stringBuilder.append("\n");

        try {
            Files.write(FileActions.getPathFromFilename("orders"), stringBuilder.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private String[] getStringsFromLine(String line) {
        if (line != null) {
            return line.split(",");
        }

        throw new RuntimeException("Empty line from file");
    }
}
