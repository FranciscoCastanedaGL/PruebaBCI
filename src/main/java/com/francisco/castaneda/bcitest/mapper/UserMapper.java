package com.francisco.castaneda.bcitest.mapper;



import com.francisco.castaneda.bcitest.model.entity.User;
import com.francisco.castaneda.bcitest.model.dto.ResponseUserDTO;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements OrikaMapperFactoryConfigurer {

    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        MapperFacade mapperFacade = orikaMapperFactory.getMapperFacade();
        orikaMapperFactory.classMap(User.class, ResponseUserDTO.class)

                .field("id","id")
                .field("name","name")
                .field("password","password")
                .field("email","email")
                .field("token","token")
                .field("isActive","isActive")
                .field("lastLogin","lastLogin")
                .field("created","created")
                .field("isAdmin","isAdmin")
                .field("phones{number}","phones{number}")
                .field("phones{cityCode}","phones{cityCode}")
                .field("phones{countryCode}","phones{countryCode}")
                .byDefault()
                .register();
    }



}