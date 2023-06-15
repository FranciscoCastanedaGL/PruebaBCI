package com.francisco.castaneda.bcitest.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorInfoDTO {
    private Timestamp timestamp;
    private Integer code;
    private String message;
}
