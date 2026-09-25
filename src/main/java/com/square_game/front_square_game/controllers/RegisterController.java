package com.square_game.front_square_game.controllers;

import com.square_game.front_square_game.services.UserApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    private final UserApiService userApiService;

    public RegisterController(UserApiService userApiService) {
        this.userApiService = userApiService;
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password, @RequestParam String email, Model model){
        try {
            userApiService.register(username, password, email);
            return "redirect:/login";
        } catch (Exception e){
            System.out.println("Erreur lors de l'inscription : "+ e.getMessage());
            model.addAttribute("error", "Impossible de créer le compte.");
            return "register";
        }
    }
}
