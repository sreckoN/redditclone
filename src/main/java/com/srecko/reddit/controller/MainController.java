package com.srecko.reddit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.Locale;

@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        String[] countries = Locale.getISOCountries();
        for (int i = 0; i < countries.length; i++) {
            Locale locale = new Locale("", countries[i]);
            countries[i] = locale.getDisplayCountry();
        }
        Arrays.sort(countries);
        model.addAttribute("countries", countries);
        return "signup";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
