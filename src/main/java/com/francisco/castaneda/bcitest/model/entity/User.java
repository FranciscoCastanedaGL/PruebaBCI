package com.francisco.castaneda.bcitest.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private String token;
    private Boolean isActive;
    private Timestamp lastLogin;
    private Timestamp created;
    @JsonIgnore
    private Boolean isAdmin ;
    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Phone> phones;

    public User (String username, String password) {
        this.email = username;
        this.password = password;
    }

    public User (String username) {
        this.name = username;
        this.password = null;
        this.isAdmin = null;
    }



}