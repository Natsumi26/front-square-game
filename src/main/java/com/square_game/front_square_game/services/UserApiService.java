package com.square_game.front_square_game.services;

import com.square_game.front_square_game.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class UserApiService {

    private final RestClient restClient;

    public UserApiService(RestClient.Builder restClientBuilder, @Value("${api.users.url}") String usersApiUrl) {
        this.restClient = restClientBuilder.baseUrl(usersApiUrl).build();
    }

    public String login(String username, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        return restClient.post()
                .uri("/auth/login")
                .body(loginRequest)
                .retrieve()
                .body(String.class);

    }

}
