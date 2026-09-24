package com.square_game.front_square_game.services;

import com.square_game.front_square_game.dto.*;
import com.square_game.front_square_game.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class GameApiService {

    private final RestClient restClient;

    public GameApiService(RestClient.Builder restClientBuilder, @Value("${api.games.url}") String gamesApiUrl) {
        this.restClient = restClientBuilder.baseUrl(gamesApiUrl).build();
    }


    public List<GameDto> getGames(Authentication authentication) {

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        String token = userDetails.getToken();

        return restClient.get()
                .uri("/games")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<List<GameDto>>() {});
    }

    public void createGame(String gameType, Integer playerCount, Integer boardSize, Set<UUID> opponentIds, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String token = userDetails.getToken();

        CreateGameRequest  createGameRequest = new CreateGameRequest();
        createGameRequest.setType(gameType);
        createGameRequest.setPlayerCount(playerCount);
        createGameRequest.setBoardSize(boardSize);
        createGameRequest.setOpponentIds(opponentIds);


        restClient.post()
                .uri("/games")
                .header("Authorization", "Bearer " + token)
                .body(createGameRequest)
                .retrieve()
                .toBodilessEntity();
    }

    public GameResponseDto getGame(UUID gameId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = userDetails.getToken();

        return restClient.get()
                .uri("/games/{gameId}", gameId)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(GameResponseDto.class);
    }

    public Set<CellPositionDto> getPossibleMoves(
            Authentication authentication,
            UUID gameId
    ) {
        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        String token = userDetails.getToken();

        return restClient.get()
                .uri("/games/{gameId}/possiblemoves", gameId)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<Set<CellPositionDto>>() {});
    }

    public Set<CellPositionDto> getPossibleMovesForToken(
            Authentication authentication,
            UUID gameId,
            int x,
            int y
    ) {
        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        String token = userDetails.getToken();

        return restClient.get()
                .uri("/games/{gameId}/tokens/{x}/{y}/possiblemoves",
                        gameId, x, y)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<Set<CellPositionDto>>() {});
    }

    public void playMove(
            Authentication authentication,
            UUID gameId,
            CellPositionDto from,
            CellPositionDto to
    ) {
        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        String token = userDetails.getToken();

        MoveParamsDto moveParams = new MoveParamsDto();
        moveParams.setFrom(from);
        moveParams.setTo(to);

        restClient.post()
                .uri("/games/{gameId}/moves", gameId)
                .header("Authorization", "Bearer " + token)
                .body(moveParams)
                .retrieve()
                .toBodilessEntity();
    }

    public void deleteGame(UUID gameId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = userDetails.getToken();
        restClient.delete()
                .uri("/games/{gameId}", gameId)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .toBodilessEntity();
    }
}
