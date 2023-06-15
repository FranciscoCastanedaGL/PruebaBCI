package com.francisco.castaneda.bcitest.model.dto;

import com.francisco.castaneda.bcitest.model.entity.Phone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private String  name;
    private String password;
    private String email;
    private List<Phone> phones;

}
