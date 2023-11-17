package com.francisco.castaneda.bcitest.mapper;


import com.francisco.castaneda.bcitest.model.dto.ResponseUserDTO;
import com.francisco.castaneda.bcitest.model.dto.UserDTO;
import com.francisco.castaneda.bcitest.model.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  private final ModelMapper modelMapper;
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ResponseUserDTO mapUserToResponseUserDTO(User user) {
        return modelMapper.map(user, ResponseUserDTO.class);
    }

    public User mapResponseUserDTOToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }


}