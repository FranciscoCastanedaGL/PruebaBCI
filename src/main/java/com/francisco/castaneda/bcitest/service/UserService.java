package com.francisco.castaneda.bcitest.service;

import com.francisco.castaneda.bcitest.exceptions.CustomException;
import com.francisco.castaneda.bcitest.model.dto.InfoUserTokenDTO;
import com.francisco.castaneda.bcitest.model.dto.ResponseUserDTO;
import com.francisco.castaneda.bcitest.model.dto.UserDTO;
import com.francisco.castaneda.bcitest.model.entity.User;

public interface UserService  {
    InfoUserTokenDTO createUser(User newUser)throws CustomException;
    ResponseUserDTO sigIn (UserDTO user);
}
