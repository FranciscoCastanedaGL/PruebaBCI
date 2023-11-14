package com.francisco.castaneda.bcitest.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.francisco.castaneda.bcitest.model.entity.Phone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseUserDTO {
    private String id;
    private Date created;
    private Date lastLogin;
    private String token;
    private Boolean isActive;
    private String name;
    private String email;
    private String password;
    @JsonIgnore
    private Boolean isAdmin ;
    private List<Phone> phones;

}
