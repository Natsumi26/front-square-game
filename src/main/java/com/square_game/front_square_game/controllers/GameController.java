package com.square_game.front_square_game.controllers;

import com.square_game.front_square_game.security.CustomUserDetails;
import com.square_game.front_square_game.services.GameApiService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        return "redirect:/";
    }
}
