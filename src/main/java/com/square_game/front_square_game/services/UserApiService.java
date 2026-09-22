package com.square_game.front_square_game.services;

import com.square_game.front_square_game.dto.LoginRequest;
import com.square_game.front_square_game.dto.UserDto;
import com.square_game.front_square_game.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collection;

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

    public Collection<UserDto> getUsers(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        System.out.println(customUserDetails);
        String token = customUserDetails.getToken();

        return restClient.get()
                .uri("/users")
                .header("Authorization", "Bearer " + token )
                .retrieve()
                .body(new ParameterizedTypeReference<Collection<UserDto>>() {});
    }

}
