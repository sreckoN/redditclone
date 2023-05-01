package com.srecko.reddit.users;

import com.srecko.reddit.users.entity.User;
import com.srecko.reddit.users.repository.UserRepository;
import java.util.Date;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Data loader.
 *
 * @author Srecko Nikolic
 */
@Configuration
@Profile("dev")
public class DataLoader implements CommandLineRunner {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  /**
   * Instantiates a new Data loader.
   *
   * @param passwordEncoder the password encoder
   * @param userRepository  the user repository
   */
  public DataLoader(PasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  @Override
  @Transactional
  public void run(String... args) {
    User user = new User("Srecko", "Nikolic", "srecko.nikolic@protonmail.com",
        "sreckon",
        passwordEncoder.encode("password"), "Serbia", true);
    user.setRegistrationDate(new Date());
    userRepository.save(user);
  }
}
