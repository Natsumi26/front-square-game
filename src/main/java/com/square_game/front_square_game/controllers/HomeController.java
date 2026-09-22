package com.square_game.front_square_game.controllers;

import com.square_game.front_square_game.dto.GameDto;
import com.square_game.front_square_game.security.CustomUserDetails;
import com.square_game.front_square_game.services.GameApiService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class HomeController {

    private final GameApiService gameApiService;

    public HomeController(GameApiService gameApiService) {
        this.gameApiService = gameApiService;
    }

    @GetMapping("/")
    public String home(Authentication authentication, Model model) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        List<GameDto> games = gameApiService.getGames(authentication);

        model.addAttribute("username",customUserDetails.getUsername());
        model.addAttribute("games", games);
        return "home";
    }

}
