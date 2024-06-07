package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.MailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;


@Controller
@RequiredArgsConstructor
public class MessageController {
    private final MailService mailService;

    @PostMapping("/sendMessage/{id}")
    public String sendMessage(@PathVariable Long id, String message, Principal principal){
        mailService.sendMessage(id, principal.getName(),message);
        return "redirect:/people/"+id;
    }
}
