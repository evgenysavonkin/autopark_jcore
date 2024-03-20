package org.evgenysav.infrastructure.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.evgenysav.infrastructure.dto.annotations.Column;
import org.evgenysav.infrastructure.dto.annotations.ID;
import org.evgenysav.infrastructure.dto.annotations.Table;

@Table(name = "types")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Types {
    @ID
    private Long id;

    @Column(name = "type_id", nullable = false)
    private Long typeId;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "tax_coefficient")
    private Double taxCoefficient;

    @Override
    public String toString() {
        return name;
    }
}
