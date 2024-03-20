package org.evgenysav.infrastructure.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.evgenysav.infrastructure.dto.annotations.Column;
import org.evgenysav.infrastructure.dto.annotations.FK;
import org.evgenysav.infrastructure.dto.annotations.ID;
import org.evgenysav.infrastructure.dto.annotations.Table;

@Table(name = "electrical_engine")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectricalStartable {
    @ID
    private Long id;

    @FK(tableName = "vehicles", id = "vehicle_id")
    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    @Column(name = "battery_size", nullable = false)
    private Double batterySize;

    @Column(name = "electricity_consumption_per_100", nullable = false)
    private Double electricityConsumptionPer100;

    @Column(name = "tax_coefficient", nullable = false)
    private Double taxCoefficient;

    @Override
    public String toString() {
        return "Engine {batterySize = " + batterySize +
                ", electricityConsumptionPer100 = " + electricityConsumptionPer100 +
                ", taxCoefficient = " + taxCoefficient +
                '}';
    }
}
