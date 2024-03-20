package org.evgenysav.infrastructure.dto.entity;

import lombok.*;
import org.evgenysav.infrastructure.dto.annotations.Column;
import org.evgenysav.infrastructure.dto.annotations.ID;
import org.evgenysav.infrastructure.dto.annotations.Table;

@Table(name = "orders")
@Builder
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    @ID
    private Long id;

    @Column(name = "vehicle_id", nullable = false, unique = true)
    private Long vehicleId;
}
