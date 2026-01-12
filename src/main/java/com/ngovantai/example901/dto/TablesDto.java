package com.ngovantai.example901.dto;

import com.ngovantai.example901.entity.Tables;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TablesDto {
    private Long id;
    private Integer number;
    private Integer capacity;
    private Tables.TableStatus status;
}
