package com.francisco.castaneda.bcitest.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfoUserTokenDTO {

    private String userName;
    private String token;
}
