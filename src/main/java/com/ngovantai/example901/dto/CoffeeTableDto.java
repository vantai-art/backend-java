package com.ngovantai.example901.dto;

import com.ngovantai.example901.entity.CoffeeTable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoffeeTableDto {
    private Long id;
    private Integer number;
    private Integer capacity;
    private CoffeeTable.TableStatus status;
}
