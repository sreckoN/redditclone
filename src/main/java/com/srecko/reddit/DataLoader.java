package com.srecko.reddit;

import com.srecko.reddit.entity.User;
import com.srecko.reddit.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@Configuration
public class DataLoader implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public DataLoader(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setUsername("sreckon");
        user.setFirstName("Srecko");
        user.setLastName("Nikolic");
        user.setEmail("srecko.nikolic@protonmail.com");
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode("password"));
        user.setRegistrationDate(new Date());
        user.setCountry("Serbia");
        userRepository.save(user);
    }
}
