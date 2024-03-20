package org.evgenysav.infrastructure.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.evgenysav.infrastructure.dto.annotations.Column;
import org.evgenysav.infrastructure.dto.annotations.ID;
import org.evgenysav.infrastructure.dto.annotations.Table;

import java.util.Date;

@Table(name = "rents")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rents {

    @ID
    private Long id;

    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    @Column(name = "rent_date", nullable = false)
    private Date rentDate;

    @Column(name = "rent_cost", nullable = false)
    private Double rentalCost;

    @Override
    public String toString() {
        return "Rent{rentDate = " + rentDate + ", rentalCost = " + rentalCost + "}";
    }
}
