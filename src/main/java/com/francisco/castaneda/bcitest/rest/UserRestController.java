package com.francisco.castaneda.bcitest.rest;

import com.francisco.castaneda.bcitest.exceptions.CustomException;
import com.francisco.castaneda.bcitest.mapper.UserMapper;
import com.francisco.castaneda.bcitest.model.entity.Phone;
import com.francisco.castaneda.bcitest.model.entity.User;
import com.francisco.castaneda.bcitest.service.UserService;
import com.francisco.castaneda.bcitest.model.dto.InfoUserTokenDTO;
import com.francisco.castaneda.bcitest.model.dto.ResponseUserDTO;
import com.francisco.castaneda.bcitest.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService ;
    private final UserMapper userMapper;

    @PostMapping("/sign-up")
    public ResponseEntity<InfoUserTokenDTO> createUser( @Valid @RequestBody UserDTO user) throws CustomException {

        User newUser =  userMapper.mapResponseUserDTOToUser(user) ;
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setName(user.getName());
        newUser.setPhones(user.getPhones());
        return new ResponseEntity<>( this.userService.createUser(newUser),HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseUserDTO login(@Valid @RequestBody UserDTO user) {
        return userService.sigIn(user);
    }

}
