package org.evgenysav.infrastructure.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.evgenysav.infrastructure.dto.annotations.Column;
import org.evgenysav.infrastructure.dto.annotations.FK;
import org.evgenysav.infrastructure.dto.annotations.ID;
import org.evgenysav.infrastructure.dto.annotations.Table;

@Table(name = "vehicle_color")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Color_ {
    @ID
    private Long id;

    @FK(tableName = "vehicles", id = "vehicle_id")
    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    @Column(name = "color", nullable = false)
    private String color;
}
