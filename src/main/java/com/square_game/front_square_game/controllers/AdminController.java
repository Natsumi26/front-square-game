package com.square_game.front_square_game.controllers;

import com.square_game.front_square_game.dto.UserDto;
import com.square_game.front_square_game.security.CustomUserDetails;
import com.square_game.front_square_game.services.UserApiService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserApiService userApiService;

    public AdminController(UserApiService userApiService) {
        this.userApiService = userApiService;
    }

    @GetMapping
    public String adminPage(Authentication authentication, Model model) {
        Collection<UserDto> users = userApiService.getUsers(authentication);

        model.addAttribute("users", users);

        return "admin";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(
            @PathVariable UUID id,
            Authentication authentication) {

        userApiService.deleteUser(authentication, id);

        return "redirect:/admin";
    }
}
