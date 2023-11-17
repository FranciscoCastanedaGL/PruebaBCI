package com.francisco.castaneda.bcitest.service.serviceimpl;

import com.francisco.castaneda.bcitest.exceptions.ValidationsException;
import com.francisco.castaneda.bcitest.repository.UserRepository;
import com.francisco.castaneda.bcitest.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {


    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final User user = userRepository.findUserByEmail(email) ;

        if (user == null) {
            throw new ValidationsException("The user don't exist","No cumple", HttpStatus.NOT_ACCEPTABLE);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password(user.getPassword())
                .authorities("ADMIN")
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

}