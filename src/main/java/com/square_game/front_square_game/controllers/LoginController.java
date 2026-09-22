package com.square_game.front_square_game.controllers;

import com.square_game.front_square_game.security.CustomUserDetails;
import com.square_game.front_square_game.services.UserApiService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final UserApiService userApiService;

    public LoginController(UserApiService userApiService) {
        this.userApiService = userApiService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,@RequestParam String password, Model model, HttpServletRequest request) {

       try{
           String token = userApiService.login(username, password);

           CustomUserDetails userDetails = new CustomUserDetails(username,token);

           UsernamePasswordAuthenticationToken  authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());


           SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
           securityContext.setAuthentication(authenticationToken);
           SecurityContextHolder.setContext(securityContext);


           request.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

           return "redirect:/";
       } catch(Exception e){
           System.out.println( "Erreur lors de la connexion : " + e.getMessage() );
           model.addAttribute("error","Nom d'utilisateur ou mot de passe incorrect.");
           return "login";
       }
    }
}
