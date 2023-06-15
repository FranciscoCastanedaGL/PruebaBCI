package com.francisco.castaneda.bcitest.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Phone {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;
    private Long number;
    private int cityCode;
    private String countryCode;

}
