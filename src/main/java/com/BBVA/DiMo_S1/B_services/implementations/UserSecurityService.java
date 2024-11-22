package com.BBVA.DiMo_S1.B_services.implementations;

import com.BBVA.DiMo_S1.C_repositories.UserRepository;
import com.BBVA.DiMo_S1.D_models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserSecurityService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserDetailsService userDetailsService() {
        return email -> {
          Optional<User> user = userRepository.findByEmail(email);

          User userAux = User.builder().build();

          if (user.isPresent()) {

              userAux = user.get();
          }
            return org.springframework.security.core.userdetails.User.builder()
                    .username(userAux.getEmail())
                    .password(userAux.getPassword())
                    .roles(userAux.getRole().getName())
                    .build();
        };
    }
}