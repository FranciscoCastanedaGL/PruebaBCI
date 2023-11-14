package com.francisco.castaneda.bcitest.security;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

 class SecurityUtilsTest extends  AbstractRestControllerTest{
    @Before("")
    public void setUp() {
        SecurityContextHolder.clearContext();
    }
    
    @Test
     void getCurrentUsername() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
        SecurityContextHolder.setContext(securityContext);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        assertThat(username).contains("admin");
    }

    @Test
     void getActualUserForUserWithoutToken() throws Exception {
        getMockMvc().perform(get("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}
