package com.square_game.front_square_game.controllers;

import com.square_game.front_square_game.dto.GameDto;
import com.square_game.front_square_game.dto.GameTypeDto;
import com.square_game.front_square_game.dto.UserDto;
import com.square_game.front_square_game.security.CustomUserDetails;
import com.square_game.front_square_game.services.GameApiService;
import com.square_game.front_square_game.services.UserApiService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class HomeController {

    private final GameApiService gameApiService;
    private final UserApiService userApiService;

    public HomeController(GameApiService gameApiService, UserApiService userApiService) {
        this.gameApiService = gameApiService;
        this.userApiService = userApiService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        List<GameDto> games = gameApiService.getGames(authentication);

        Collection<UserDto> users = userApiService.getUsers(authentication);

        Collection<GameTypeDto> listGames = gameApiService.getListGame();

        model.addAttribute("username",customUserDetails.getUsername());
        model.addAttribute("games", games);
        model.addAttribute("users", users);
        model.addAttribute("listGames", listGames);

        return "home";
    }

}
