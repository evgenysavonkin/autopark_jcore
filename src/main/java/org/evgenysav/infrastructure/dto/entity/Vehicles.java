package org.evgenysav.infrastructure.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.evgenysav.classes.Color;
import org.evgenysav.classes.Startable;
import org.evgenysav.classes.VehicleType;
import org.evgenysav.infrastructure.dto.annotations.Column;
import org.evgenysav.infrastructure.dto.annotations.ID;
import org.evgenysav.infrastructure.dto.annotations.Table;

import java.util.List;

@Table(name = "vehicles")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicles {
    @ID
    private Long id;

    @Column(name = "vehicle_id", nullable = false, unique = true)
    private Long vehicleId;

    @Column(name = "vehicle_type_id", nullable = false)
    private Long vehicleTypeId;

    private List<Rents> machineOrders;

    private Startable startable;
    private VehicleType type;

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @Column(name = "registration_number", nullable = false)
    private String registrationNumber;

    @Column(name = "weight", nullable = false)
    private Long weight;

    @Column(name = "manufacture_year", nullable = false)
    private Long manufactureYear;

    @Column(name = "mileage", nullable = false)
    private Long mileage;

    private Color color;

    @Column(name = "tank_volume", nullable = false)
    private Long tankVolume;
    private boolean isRented;
    private long defectCount;

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId = " + vehicleId +
                ", machineOrders = " + machineOrders +
                ", engine = " + startable +
                ", type = " + type +
                ", modelName = '" + modelName + '\'' +
                ", registrationNumber = '" + registrationNumber + '\'' +
                ", weight = " + weight +
                ", manufactureYear = " + manufactureYear +
                ", mileage = " + mileage +
                ", color = " + color +
                ", tankVolume = " + tankVolume +
                '}';
    }
}
