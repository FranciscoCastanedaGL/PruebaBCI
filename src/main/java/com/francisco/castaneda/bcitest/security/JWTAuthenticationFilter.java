package com.francisco.castaneda.bcitest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.francisco.castaneda.bcitest.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private UserRepository userRepository;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("api/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            if (userRepository == null) {
                ServletContext servletContext = req.getServletContext();
                WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
                userRepository = webApplicationContext.getBean(UserRepository.class);
            }

            com.francisco.castaneda.bcitest.model.entity.User user = new ObjectMapper()
                    .readValue(req.getInputStream(), com.francisco.castaneda.bcitest.model.entity.User.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                           user.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String token = JWTRepository.getInstance().create(((User) auth.getPrincipal()).getUsername());

        JWTRepository.getInstance().addToken(token);

        com.francisco.castaneda.bcitest.model.entity.User user = userRepository
                .findUserByEmail(((User) auth.getPrincipal()).getUsername());

        if (user!=null) {
            user.setLastLogin(new Timestamp(System.currentTimeMillis()));
            user.setToken(JWTRepository.getInstance().create(user.getEmail()));
            userRepository.save(user );
        }

        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
    }
}