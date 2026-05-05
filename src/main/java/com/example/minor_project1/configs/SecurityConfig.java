package com.example.minor_project1.configs;

import com.example.minor_project1.models.Authority;
import com.example.minor_project1.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * When someone tries to log in, use my UserService + this PasswordEncoder to verify them.
     * This returns an AuthenticationManager, which is the entry point for authentication.
     * */
    @Bean
    public AuthenticationManager authenticationManager(UserService userService, PasswordEncoder passwordEncoder){
        /// DAO = Data Access Object - It handles: * Fetching user from DB; Comparing password; Creating authenticated object
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userService);
        /// This tells Spring to Use this logic to verify passwords - Internally: passwordEncoder.matches(rawPassword, encodedPassword)
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        /// This is the default implementation of AuthenticationManager (interface)
        return new ProviderManager(authenticationProvider);
    }

    /**
       Here, we define rules, Spring builds a filter chain. This enables every request must be intercepted BEFORE controller or even dispatcher.
       NOTE: authorizeHttpRequests - It executes the matchers sequentially. Therefore, never mess up with the orders.
            e.g. if we write requestMatchers("/students/**") before requestMatchers("/students/admin/**") - the first one already permits all the URL starting with /students/
     * */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/students/admin/**").hasAuthority(Authority.ADMIN.name())
                                .requestMatchers("/students/**").hasAuthority(Authority.STUDENT.name())
                                .requestMatchers("/admin/**").hasAuthority(Authority.ADMIN.name())
                                .requestMatchers(HttpMethod.GET, "/books/**").hasAnyAuthority(Authority.ADMIN.name(), Authority.STUDENT.name())
                                .requestMatchers("/books/**").hasAnyAuthority(Authority.ADMIN.name())
                                .requestMatchers("/transactions/**").hasAnyAuthority(Authority.STUDENT.name())
                                .requestMatchers("/students/**").permitAll() /// Can't write this before any of the matchers as it will invalidate all the others.
                ).httpBasic(Customizer.withDefaults());

                return http.build();
    }
}
