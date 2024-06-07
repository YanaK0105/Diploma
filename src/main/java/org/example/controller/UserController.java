package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserDto;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Objects;

@Controller
@RequestMapping("/people")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    //отображение информации о выбранном пользователе
    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id,Principal principal, Model model){
        if(principal != null){
            try {
                model.addAttribute("currentUser", userService.findByEmail(principal.getName()));
            }
            catch (UsernameNotFoundException e){
                throw new IllegalStateException("An authorized user, but these credentials were not found in the database.");
            }
        }
        model.addAttribute("user", userService.findById(id));
        return "people/show";
    }

    //отображение информации о текущем пользователе
    @GetMapping("/show")
    public String showCurrentUser(Principal principal, Model model){
        if(principal != null){
            try {
                model.addAttribute("user", userService.findByEmail(principal.getName()));
                model.addAttribute("currentUser", userService.findByEmail(principal.getName()));
            }
            catch (UsernameNotFoundException e){
                throw new IllegalStateException("An authorized user, but these credentials were not found in the database.");
            }
        }
        else{
            return "redirect:/login";
        }
        return "people/show";
    }


    //страница создания
    @GetMapping("/userCreate")
    public String createUserForm(User user, Model model){
        model.addAttribute("userRegisterResult", UserDto.builder()
                .error(false)
                .build());
        return "people/userCreate";
    }
    //создание
    @PostMapping("/userCreate")
    public String createUser(@Valid User user, BindingResult bindingResult, Model model) {
        model.addAttribute("userRegisterResult", UserDto.builder()
                .error(false)
                .build());
        if (bindingResult.hasErrors())
            return "people/userCreate";

        var result = userService.saveUser(user);
        if(result.isError()) {
            model.addAttribute("userRegisterResult", result);
            return "people/userCreate";
        }

        return "redirect:/login";
    }
    //обновление
    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id, @Valid User user, BindingResult bindingResult, Principal principal, Model model, HttpServletRequest request, HttpServletResponse response){
        model.addAttribute("userUpdateResult", UserDto.builder()
                .error(false)
                .build());

        if (bindingResult.hasErrors())
            return "people/edit";
        var currentUser = userService.findByEmail(principal.getName());
        var userMailBefore = userService.findById(id).getEmail();
        var userPassBefore= userService.findById(id).getPassword();
        if(currentUser.getRole() == Role.ADMIN || Objects.equals(currentUser.getId(), id)){
            var result = userService.updateUser(id, user);
            if(!result.isError()) {
                if (Objects.equals(user.getId(), id) && (!user.getEmail().equals(userMailBefore) || !user.getPassword().equals(userPassBefore))) {
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    if (auth != null) {
                        new SecurityContextLogoutHandler().logout(request, response, auth);
                    }
                    return "redirect:/login";
                }
            }
            if(result.isError()) {
                model.addAttribute("userUpdateResult", result);
                return "people/edit";
            }
        }
        else{
            throw new IllegalStateException("This user can't perform this action");
        }

        return "redirect:/people/"+id;
    }
    //удаление
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, Principal principal, HttpServletRequest request, HttpServletResponse response){
        var user = userService.findByEmail(principal.getName());
        userService.deleteUser(id);
        if (Objects.equals(user.getId(), id)) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
            return "redirect:/login";
        }
        return "redirect:/index";
    }

    //страница обновления
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("userUpdateResult", UserDto.builder()
                .error(false)
                .build());
        model.addAttribute("user", userService.findById(id));
        return "people/edit";
    }
}
