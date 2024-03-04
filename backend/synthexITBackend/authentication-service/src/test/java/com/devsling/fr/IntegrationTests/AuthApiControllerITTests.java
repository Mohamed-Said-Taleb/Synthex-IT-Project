package com.devsling.fr.IntegrationTests;

import com.devsling.fr.controller.AuthController;
import com.devsling.fr.dto.requests.LoginFormRequest;
import com.devsling.fr.dto.requests.SignUpFormRequest;
import com.devsling.fr.dto.responses.GetTokenResponse;
import com.devsling.fr.dto.responses.RegisterResponse;
import com.devsling.fr.service.AuthService;
import com.devsling.fr.tools.Constants;
import com.devsling.fr.tools.RoleName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@WebFluxTest(AuthController.class)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class AuthApiControllerITTests {


    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthService authService;

    private final static String BASIC_AUTH_PATH="/auth";

    @Test
    public void registerOkTestWithStatus200() {
        SignUpFormRequest request = SignUpFormRequest.builder()
                .username("test")
                .email("test@gmail.com")
                .password("Test123456**")
                .role_Name(RoleName.CANDIDATE.name())
                .build();

        RegisterResponse response = RegisterResponse.builder()
                .message(Constants.USER_REGISTERED_SUCCESSFULLY)
                .build();

        Mockito.when(authService.signup(any(SignUpFormRequest.class)))
                .thenReturn(Mono.just(response));

        webTestClient.post()
                .uri(BASIC_AUTH_PATH+"/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(RegisterResponse.class)
                .isEqualTo(response);
    }

    @Test
    public void loginOkTestWithStatus200() {
        LoginFormRequest request = LoginFormRequest.builder()
                .username("test")
                .password("Test123456**")
                .build();

        GetTokenResponse response =GetTokenResponse.builder()
                .token("token")
                .message("Authentication successful")
                .build();

        Mockito.when(authService.getToken(any(LoginFormRequest.class)))
                .thenReturn(Mono.just(response));

        webTestClient.post()
                .uri(BASIC_AUTH_PATH+"/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(GetTokenResponse.class)
                .isEqualTo(response);
    }

    @Test
    public void passwordRequestOkTestWithStatus200() {


    }

    @Test
    public void activateAccountOkTestWithStatus200() {


    }
}