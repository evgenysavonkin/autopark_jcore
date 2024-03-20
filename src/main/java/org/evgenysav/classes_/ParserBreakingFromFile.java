package org.evgenysav.classes_;

import org.evgenysav.classes.Vehicle;
import org.evgenysav.util.FileActions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Map;

public class ParserBreakingFromFile {

    private static final StringBuilder stringBuilder = new StringBuilder();

    public void printMapToFIle(Vehicle vehicle, Map<String, Integer> map) {
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


    public String[] getStringsFromLine(String line) {
        if (line != null) {
            return line.split(",");
        }

        throw new RuntimeException("Empty line from file");
    }
}
