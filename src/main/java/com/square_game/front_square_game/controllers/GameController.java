package com.square_game.front_square_game.controllers;

import com.square_game.front_square_game.dto.CellPositionDto;
import com.square_game.front_square_game.dto.GameDto;
import com.square_game.front_square_game.dto.GameResponseDto;
import com.square_game.front_square_game.dto.MoveParamsDto;
import com.square_game.front_square_game.security.CustomUserDetails;
import com.square_game.front_square_game.services.GameApiService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Controller
public class GameController {

    private final GameApiService gameApiService;

    public GameController(GameApiService gameApiService) {
        this.gameApiService = gameApiService;
    }


    @PostMapping("/games")
    public String createGame(        @RequestParam String gameType,
                                     @RequestParam Integer playerCount,
                                     @RequestParam Integer boardSize,
                                     @RequestParam(required = false) UUID opponentId,
                                     Authentication authentication) {
        System.out.println("opponentId = " + opponentId);
        Set<UUID> opponents = new HashSet<>();

        if (opponentId != null) {
            opponents.add(opponentId);
        }

        gameApiService.createGame(gameType, playerCount, boardSize, opponents, authentication);

        return "redirect:/home";
    }

    @GetMapping("/games/{gameId}")
    public String showGame(@PathVariable("gameId") UUID gameId, Authentication authentication, Model model) {

        GameResponseDto response = gameApiService.getGame(gameId, authentication);

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        model.addAttribute("game", response.getGame());
        model.addAttribute("winnerId", response.getWinnerId());
        model.addAttribute("userId", userDetails.getUserId());

        return "game";
    }

    // Route pour tictactoe et connectFour
    @GetMapping("/games/{gameId}/possiblemoves")
    @ResponseBody
    public Set<CellPositionDto> getPossibleMoves(
            @PathVariable UUID gameId,
            Authentication authentication
    ) {
        return gameApiService.getPossibleMoves(authentication, gameId);
    }

//    route pour le taquin
    @GetMapping("/games/{gameId}/tokens/{x}/{y}/possiblemoves")
    @ResponseBody
    public Set<CellPositionDto> getPossibleMovesForToken(
            @PathVariable UUID gameId,
            @PathVariable int x,
            @PathVariable int y,
            Authentication authentication
    ) {
        return gameApiService.getPossibleMovesForToken(
                authentication,
                gameId,
                x,
                y
        );
    }

    @PostMapping("/games/{gameId}/moves")
    @ResponseBody
    public void playMove(
            @PathVariable UUID gameId,
            @RequestBody MoveParamsDto moveParams,
            Authentication authentication
    ) {

        gameApiService.playMove(
                authentication,
                gameId,
                moveParams.getFrom(),
                moveParams.getTo()
        );
    }

    @PostMapping("/games/{gameId}/delete")
    public String deleteGame(@PathVariable UUID gameId, Authentication authentication) {
        gameApiService.deleteGame(gameId, authentication);

        return "redirect:/home";
    }

}
