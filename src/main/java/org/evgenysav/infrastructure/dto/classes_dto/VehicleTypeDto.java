package org.evgenysav.infrastructure.dto.classes_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleTypeDto {
    private long vehicleId;
    private String name;
    private double TaxCoefficient;
}
