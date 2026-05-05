package com.example.minor_project1.services;

import com.example.minor_project1.configs.PasswordEncoderConfig;
import com.example.minor_project1.models.Authority;
import com.example.minor_project1.models.User;
import com.example.minor_project1.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * FLOW:
 *      StudentService --------------> UserService
 *      AdminService --------------> UserService
 * */
@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
   private PasswordEncoder passwordEncoder;

    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username).orElse(null);
    }

    public User create(User user, Authority authority){
        //String encodedPassword = passwordEncoder.encode(user.getPassword());
        //user.setPassword(encodedPassword);
        /***
         * How to set the authority???
         * This is a generic function of creating a user. Here, we can't do ---> user.setAuthority()
         * We will need to pass this info from the Student Service class.
          */
        user.setAuthorities(authority);
        return this.userRepository.save(user);

    }
}
