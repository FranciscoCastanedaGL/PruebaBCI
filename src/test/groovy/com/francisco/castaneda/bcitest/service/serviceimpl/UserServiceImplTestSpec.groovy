package com.francisco.castaneda.bcitest.service.serviceimpl

import com.francisco.castaneda.bcitest.exceptions.ValidationsException
import com.francisco.castaneda.bcitest.mapper.UserMapper
import com.francisco.castaneda.bcitest.model.dto.UserDTO
import com.francisco.castaneda.bcitest.model.entity.User
import com.francisco.castaneda.bcitest.repository.UserRepository
import com.francisco.castaneda.bcitest.security.JWTRepository
import com.francisco.castaneda.bcitest.service.UserService
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification
import spock.lang.Unroll

class UserServiceImplTestSpec extends Specification {


    private UserService userService
    private UserRepository userRepository = Mock(UserRepository)
    private JWTRepository jwtRepository = Mock(JWTRepository)
    private UserMapper userMapper = Mock(UserMapper)
    private PasswordEncoder passwordEncoder = Mock(PasswordEncoder)

    def setup() {
        userService = new UserServiceImpl(
                userRepository,
                jwtRepository,
                userMapper,
                passwordEncoder
        )
    }

    @Unroll
    def "CreateUser should create a new user"() {

        given:
        def newUser = new User(email: "test@example.com", password: "password")
        def authentication = Mock(Authentication)
        authentication.isAuthenticated() >> true
        SecurityContextHolder.getContext().setAuthentication(authentication)

        when:
        def result = userService.createUser(newUser)

        then:
        1 * userService.jwtRepository.create(newUser.email) >> "mocked-jwt"
        1 * userService.userRepository.findUserByEmailAndIsActive(newUser.email, true) >> Optional.empty()
        1 * userService.userRepository.save(_) >> newUser
        result.token == "Bearer mocked-jwt"
        result.userName == newUser.name
    }

    @Unroll
    def "createUser should throw ValidationsException if email already exists"() {
        given:
        def existingUser = new User(email: "existing@example.com", password: "password")
        def authentication = Mock(Authentication)
        authentication.isAuthenticated() >> true
        SecurityContextHolder.getContext().setAuthentication(authentication)
        userService.userRepository.findUserByEmailAndIsActive(existingUser.email, true) >> Optional.of(existingUser)
        userService.jwtRepository.create(existingUser.email) >> "Bearer mocked-jwt"

        when:
         userService.createUser(existingUser)

        then:
        thrown(ValidationsException)

    }

    @Unroll
    def "signIn should return ResponseUserDTO on successful login"() {
        given:
        def userRequest = new UserDTO(email: "test@example.com", password: "password")
        def existingUser = new User(email: "test@example.com", password: "encoded-password")
        userService.userRepository.findUserByEmailAndIsActive(userRequest.email, true) >> Optional.of(existingUser)
        userService.passwordEncoder.matches(userRequest.password, existingUser.password) >> true
        userService.jwtRepository.create(userRequest.email) >> "mocked-jwt"

        when:
        def result = userService.sigIn(userRequest)

        then:
        1 * userService.userRepository.save(existingUser) >> existingUser

    }

    def "signIn should throw ValidationsException on incorrect password"() {
        given:
        def userRequest = new UserDTO(email: "test@example.com", password: "incorrect-password")
        def existingUser = new User(email: "test@example.com", password: "encoded-password")
        userService.userRepository.findUserByEmailAndIsActive(userRequest.email, true) >> Optional.of(existingUser)
        userService.passwordEncoder.matches(userRequest.password, existingUser.password) >> false

        when:
        userService.sigIn(userRequest)

        then:
        thrown ValidationsException
    }
}
