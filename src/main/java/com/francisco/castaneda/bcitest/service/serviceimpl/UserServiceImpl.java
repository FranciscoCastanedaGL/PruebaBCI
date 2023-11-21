package com.francisco.castaneda.bcitest.service.serviceimpl;

import com.francisco.castaneda.bcitest.exceptions.CustomException;
import com.francisco.castaneda.bcitest.exceptions.ValidationsException;
import com.francisco.castaneda.bcitest.mapper.UserMapper;
import com.francisco.castaneda.bcitest.model.dto.InfoUserTokenDTO;
import com.francisco.castaneda.bcitest.model.dto.ResponseUserDTO;
import com.francisco.castaneda.bcitest.model.dto.UserDTO;
import com.francisco.castaneda.bcitest.model.entity.User;
import com.francisco.castaneda.bcitest.repository.UserRepository;
import com.francisco.castaneda.bcitest.security.JWTRepository;
import com.francisco.castaneda.bcitest.security.SecurityConstants;
import com.francisco.castaneda.bcitest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private  final JWTRepository jwtRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public InfoUserTokenDTO createUser(User newUser) throws CustomException {

        //Se asigna el UUID
        String uuid = java.util.UUID.randomUUID().toString();
        //Se crea el token
        String jwt =this.jwtRepository.create(newUser.getEmail());
        //se obtiene la autenticación
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        newUser.setUuID(uuid);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setIsActive(true);
        newUser.setToken(SecurityConstants.TOKEN_PREFIX.concat(jwt));
        newUser.setIsAdmin(auth.isAuthenticated());
        newUser.setCreated(new Timestamp(System.currentTimeMillis()));
        User optionalUser =  this.userRepository.findUserByEmailAndIsActive(newUser.getEmail(),newUser.getIsActive()).orElse(null);

         if (Objects.isNull(optionalUser)){
            this.userRepository.save(newUser);
             return InfoUserTokenDTO.builder()
                             .token(newUser.getToken())
                             .userName(newUser.getName()).build();
         }else{
             throw new ValidationsException("This Email exist in the Data Base","Error",HttpStatus.NOT_ACCEPTABLE);
         }

    }

    @Override
    public ResponseUserDTO sigIn (final UserDTO userRequest){
        try {
            User userSearch =  this.userRepository.findUserByEmailAndIsActive(userRequest.getEmail(),true)
                    .orElseThrow(()-> new ValidationsException("Error to Find the User","No cumple",HttpStatus.NOT_ACCEPTABLE));
            if (passwordEncoder.matches(userRequest.getPassword(), userSearch.getPassword())) {
               userSearch.setLastLogin(new Timestamp(System.currentTimeMillis()));
               userSearch.setToken(jwtRepository.create(userRequest.getEmail()));
               User userResponse =  userRepository.save(userSearch);
               return userMapper.mapUserToResponseUserDTO(userResponse);
           }else{
               throw new ValidationsException("Password is Inscorrect","No cumple",HttpStatus.NOT_ACCEPTABLE);
           }

        } catch (AuthenticationException e) {
            throw new ValidationsException("Invalid username/password supplied","No cumple",HttpStatus.NOT_ACCEPTABLE);
        }
    }
}