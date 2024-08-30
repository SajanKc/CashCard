package com.iamsajan.cashcardservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authCustomizer -> authCustomizer
                        .requestMatchers(new AntPathRequestMatcher("/cashcards/**")).hasRole("CARD-OWNER")
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    UserDetailsService testOnlyUsers(PasswordEncoder passwordEncoder) {
        User.UserBuilder users = User.builder();
        UserDetails sajan = users.username("sajan")
                .password(passwordEncoder.encode("1234"))
                .roles("CARD-OWNER")
                .build();

        UserDetails asmitaOwnsNoCards = users
                .username("asmita-owns-no-cards")
                .password(passwordEncoder.encode("qrs456"))
                .roles("NON-OWNER")
                .build();

        UserDetails asmita = users
                .username("asmita")
                .password(passwordEncoder.encode("4321"))
                .roles("CARD-OWNER")
                .build();

        return new InMemoryUserDetailsManager(sajan, asmitaOwnsNoCards, asmita);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}