package org.evgenysav.infrastructure.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.evgenysav.infrastructure.dto.annotations.Column;
import org.evgenysav.infrastructure.dto.annotations.FK;
import org.evgenysav.infrastructure.dto.annotations.ID;
import org.evgenysav.infrastructure.dto.annotations.Table;

@Table(name = "combustion_engine")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CombustionStartable {
    @ID
    private Long id;

    @FK(tableName = "vehicles", id = "vehicle_id")
    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    @Column(name = "engine_type", nullable = false)
    private String engineType;

    @Column(name = "engine_capacity", nullable = false)
    private Double engineCapacity;

    @Column(name = "fuel_tank_capacity", nullable = false)
    private Double fuelTankCapacity;

    @Column(name = "fuel_consumption_per_100", nullable = false)
    private Double fuelConsumptionPer100;

    @Column(name = "tax_coefficient", nullable = false)
    private Double taxCoefficient;

    @Override
    public String toString() {
        return "Engine {engineType = " + engineType +
                ", engineCapacity = " + engineCapacity +
                ", fuelTankCapacity = " + fuelTankCapacity +
                ", fuelConsumptionPer100 = " + fuelConsumptionPer100 +
                ", taxCoefficient = " + taxCoefficient +
                '}';
    }
}
