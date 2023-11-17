package com.francisco.castaneda.bcitest.repository;

import com.francisco.castaneda.bcitest.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
     User findUserByEmail(String email);
    Optional<User>findUserByEmailAndIsActive(String email,boolean active);
}