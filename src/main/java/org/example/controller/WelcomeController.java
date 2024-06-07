package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class WelcomeController {
    private final UserService userService;

    @RequestMapping("/people/show")
    public String showPeople(){
        return "/people/show";
    }

    @RequestMapping("/")
    public String goToIndex(){
        return "redirect:/index";
    }

    @RequestMapping("/index")
    public String showIndex(Principal principal, Model model){
        if(principal != null){
            try {
                model.addAttribute("user", userService.findByEmail(principal.getName()));
            }
            catch (UsernameNotFoundException e){
                throw new IllegalStateException("An authorized user, but these credentials were not found in the database.");
            }
        }
        model.addAttribute("users", userService.findAll());
        return "/index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
}
