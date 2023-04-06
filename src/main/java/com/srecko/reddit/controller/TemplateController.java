package com.srecko.reddit.controller;

import com.srecko.reddit.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The type Template controller.
 *
 * @author Srecko Nikolic
 */
@Controller
@RequestMapping("/")
public class TemplateController {

  private final UserService userService;

  private static final Logger logger = LogManager.getLogger(TemplateController.class);

  /**
   * Instantiates a new Template controller.
   *
   * @param userService the user service
   */
  public TemplateController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Index string.
   *
   * @param model     the model
   * @return the string
   */
  @GetMapping
  public String index(Model model) {
    logger.info("Returning index.html");
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("loggedIn", authentication.isAuthenticated());
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