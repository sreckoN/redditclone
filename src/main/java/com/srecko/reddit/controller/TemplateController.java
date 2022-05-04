package com.srecko.reddit.controller;

import com.srecko.reddit.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class TemplateController {

    private final UserService userService;

    public TemplateController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(Principal principal, Model model) {
        boolean loggedIn = principal != null;
        model.addAttribute("loggedIn", loggedIn);
        return "index";
    }

    /*@GetMapping("/signup")
    public String signup(Model model,
                         @RequestParam(required = false) boolean usernameError,
                         @RequestParam(required = false) boolean emailError) {
        String[] countries = Locale.getISOCountries();
        for (int i = 0; i < countries.length; i++) {
            Locale locale = new Locale("", countries[i]);
            countries[i] = locale.getDisplayCountry();
        }
        Arrays.sort(countries);
        model.addAttribute("countries", countries);
        model.addAttribute("newUser", new User());
        if (usernameError) model.addAttribute("usernameError", true);
        if (emailError) model.addAttribute("emailError", true);
        return "signup";
    }

    @PostMapping("/signup")
    public String signupForm(@ModelAttribute User user) {
        if (userService.isUsernameAlreadyInUse(user.getUsername())) {
            return "redirect:/signup?usernameError=true";
        }
        if (userService.isEmailAlreadyInUse(user.getEmail())) {
            return "redirect:/signup?emailError=true";
        }
        user.setEnabled(true);
        userService.saveUser(user);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }*/
}