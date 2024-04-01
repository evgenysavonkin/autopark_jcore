package org.evgenysav.infrastructure.dto.classes_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDto {
    public int typeId;
    private String typeName;
    private double taxCoefficient;
    private String modelName;
    private int manufactureYear;
    private String registrationNumber;
    private double weight;
    private int mileage;
    private String color;
    private double tankVolume;
    private String engineName;
    private double engineTaxCoefficient;
    private int id;
    private long vehicleId;
    private double per100km;
    private double maxKm;
    private double tax;
}
