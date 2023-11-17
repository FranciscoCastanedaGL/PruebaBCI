package com.francisco.castaneda.bcitest.mapper;

import com.francisco.castaneda.bcitest.exceptions.CustomException;
import com.francisco.castaneda.bcitest.model.dto.ErrorInfoDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ExceptionMapper {

    private final ModelMapper modelMapper;

    public ExceptionMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ErrorInfoDTO mapToErrorInfoDTO(CustomException ex, Class<ErrorInfoDTO> errorInfoDTOClass) {
        return modelMapper.map(ex, errorInfoDTOClass);
    }
}
