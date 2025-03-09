package pl.sankouski.boarditfront.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.sankouski.boarditfront.security.RegistrationDto;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestTemplate restTemplate;


    private static final String API_URL = "http://localhost:8080/auth/signup";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeExists("registrationDto"));
    }

    @Test
    void testRegisterUserSuccess() throws Exception {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setLogin("testuser");
        registrationDto.setPassword("password");

        mockMvc.perform(post("/registration")
                        .flashAttr("registrationDto", registrationDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login"));

        verify(restTemplate, times(1)).postForEntity(eq(API_URL), eq(registrationDto), eq(String.class));
    }


    @Test
    void testRegisterUserConflictError() throws Exception {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setLogin("existinguser");
        registrationDto.setPassword("password");

        String conflictMessage = "{\"message\":\"User already exists.\"}";
        when(restTemplate.postForEntity(eq(API_URL), eq(registrationDto), eq(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.CONFLICT, "", conflictMessage.getBytes(), null));

        mockMvc.perform(post("/registration")
                        .flashAttr("registrationDto", registrationDto))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attribute("error", "User already exists."));

        verify(restTemplate, times(1)).postForEntity(eq(API_URL), eq(registrationDto), eq(String.class));
    }

    @Test
    void testRegisterUserUnexpectedError() throws Exception {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setLogin("testuser");
        registrationDto.setPassword("password");

        when(restTemplate.postForEntity(eq(API_URL), eq(registrationDto), eq(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        mockMvc.perform(post("/registration")
                        .flashAttr("registrationDto", registrationDto))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attribute("error", "An unexpected error occurred."));

        verify(restTemplate, times(1)).postForEntity(eq(API_URL), eq(registrationDto), eq(String.class));
    }
}