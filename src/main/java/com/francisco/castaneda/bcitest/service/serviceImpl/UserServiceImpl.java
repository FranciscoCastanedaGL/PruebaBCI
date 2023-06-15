package com.francisco.castaneda.bcitest.service.serviceImpl;

import com.francisco.castaneda.bcitest.exceptions.CustomException;
import com.francisco.castaneda.bcitest.exceptions.ValidationsException;
import com.francisco.castaneda.bcitest.model.constants.CustomConstants;
import com.francisco.castaneda.bcitest.repository.UserRepository;
import com.francisco.castaneda.bcitest.security.JWTRepository;
import com.francisco.castaneda.bcitest.security.SecurityConstants;
import com.francisco.castaneda.bcitest.model.dto.InfoUserTokenDTO;
import com.francisco.castaneda.bcitest.model.dto.ResponseUserDTO;
import com.francisco.castaneda.bcitest.model.dto.UserDTO;
import com.francisco.castaneda.bcitest.model.entity.User;
import com.francisco.castaneda.bcitest.service.UserService;
import com.francisco.castaneda.bcitest.utils.Validations;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

import static com.francisco.castaneda.bcitest.utils.JasyptUtil.decyptPwd;
import static com.francisco.castaneda.bcitest.utils.JasyptUtil.encyptPwd;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private  final JWTRepository jwtRepository;
    private final MapperFacade orikaMapper;

    @Override
    public InfoUserTokenDTO createUser(User newUser) throws CustomException {
        String uuid = java.util.UUID.randomUUID().toString();
        newUser.setId(uuid);
        String jwt =this.jwtRepository.create(newUser.getEmail());

        if (!Validations.emailValidation(newUser.getEmail())){
              throw new ValidationsException("No se puede validar el mail","No cumple",HttpStatus.NOT_ACCEPTABLE);}
        if(!Validations.paswordValidation(newUser.getPassword())){
            throw new ValidationsException("La Password No cumple con los Standares de Aceptacion","No cumple",HttpStatus.NOT_ACCEPTABLE);}
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        newUser.setPassword(encyptPwd(CustomConstants.SEED_ENCRYPTION,newUser.getPassword()));
        newUser.setIsActive(true);
        newUser.setToken(SecurityConstants.TOKEN_PREFIX+jwt);
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
            Optional<User> optionalUser =  this.userRepository.findUserByEmailAndIsActive(userRequest.getEmail(),true);
            User userSearch = optionalUser.orElseThrow(()-> new ValidationsException("Error to Find the User","No cumple",HttpStatus.NOT_ACCEPTABLE) );
           String decrypt = decyptPwd(CustomConstants.SEED_ENCRYPTION,userSearch.getPassword());
            if ((userRequest.getPassword()).equals(decrypt)) {
               userSearch.setLastLogin(new Timestamp(System.currentTimeMillis()));
               userSearch.setToken(jwtRepository.create(userRequest.getEmail()));
               User userResponse = this.userRepository.save(userSearch);
               return orikaMapper.map(userResponse, ResponseUserDTO.class);
           }else{
               throw new ValidationsException("Password is Inscorrect","No cumple",HttpStatus.NOT_ACCEPTABLE);
           }

        } catch (AuthenticationException e) {
            throw new ValidationsException("Invalid username/password supplied","No cumple",HttpStatus.NOT_ACCEPTABLE);
        }
    }
}