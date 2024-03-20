package org.evgenysav.infrastructure.dto.entity;

import lombok.*;
import org.evgenysav.infrastructure.dto.annotations.Column;
import org.evgenysav.infrastructure.dto.annotations.FK;
import org.evgenysav.infrastructure.dto.annotations.ID;
import org.evgenysav.infrastructure.dto.annotations.Table;

@Table(name = "order_entries")
@Builder
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntries {

    @ID
    private Long id;

    @FK(tableName = "orders", id = "vehicle_id")
    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    @Column(name = "detail_name", nullable = false)
    private String detailName;

    @Column(name = "quantity", nullable = false)
    private Long quantity;
}
