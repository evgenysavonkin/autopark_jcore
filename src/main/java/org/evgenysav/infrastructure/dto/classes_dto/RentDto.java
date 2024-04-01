package org.evgenysav.infrastructure.dto.classes_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentDto {
    private long id;
    private long vehicleId;
    private Date rentDate;
    private double rentalCost;
}
