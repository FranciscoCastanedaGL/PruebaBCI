package com.francisco.castaneda.bcitest.service.serviceImpl

import com.francisco.castaneda.bcitest.BaseITSpec
import com.francisco.castaneda.bcitest.model.entity.Phone
import com.francisco.castaneda.bcitest.model.entity.User
import com.francisco.castaneda.bcitest.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired

import spock.lang.Subject


class UserServiceImplTest extends BaseITSpec {
/*
    @Autowired
    @Subject
    UserRepository userRepository

    def "Should properly CreateUser"() {
        given:
        def user = User.builder()
                .email("francisco@globallogic.com")
                .isActive(true)
                .isAdmin(true)
                .password("asdasddsad")
                .name("francisco")
                .phones(Collections.addAll(Phone.builder().countryCode("Argentina").number(1111111).cityCode(011).build))
                .build()

        when:
        def  result  =  userRepository.save(user)

        then:
        with(result) {
            name == "francisco"
        }

    }*/

}
