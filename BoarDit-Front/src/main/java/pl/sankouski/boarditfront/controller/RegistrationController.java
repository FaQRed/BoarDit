package pl.sankouski.boarditfront.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.sankouski.boarditfront.security.RegistrationDto;

import javax.validation.Valid;


@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final RestTemplate restTemplate;
    @Value("${api.url}")
    String apiUrl = "http://localhost:8080";


    public RegistrationController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public String registerUser(
            @ModelAttribute("registrationDto") @Valid RegistrationDto registrationDto,
            BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        String url = apiUrl + "/auth/signup";
        try {
            restTemplate.postForEntity(url, registrationDto, String.class);
            return "redirect:/auth/login";
        } catch (HttpClientErrorException ex) {
            String errorMessage = "An unexpected error occurred.";
            if (ex.getStatusCode() == HttpStatus.CONFLICT) {
                try {
                    errorMessage = new ObjectMapper()
                            .readTree(ex.getResponseBodyAsString())
                            .get("message")
                            .asText();
                } catch (JsonProcessingException e) {
                    errorMessage = "Error parsing error message.";
                }
            }
            model.addAttribute("error", errorMessage);
            return "registration";
        }
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationDto", new RegistrationDto());
        return "registration";
    }
}