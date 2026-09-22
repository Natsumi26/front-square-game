package com.square_game.front_square_game.controllers;

import com.square_game.front_square_game.security.CustomUserDetails;
import com.square_game.front_square_game.services.GameApiService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GameController {

    private final GameApiService gameApiService;

    public GameController(GameApiService gameApiService) {
        this.gameApiService = gameApiService;
    }


    @PostMapping("/games")
    public String createGame(@RequestParam String gameType, Authentication authentication) {

        gameApiService.createGame(gameType, authentication);

        return "redirect:/";
    }
}
