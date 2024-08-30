package com.iamsajan.cashcardservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeRequests((authz) ->
                        authz
                                .antMatchers("/cashcards/**").hasRole("CARD-OWNER")
                                .antMatchers("/h2-console/**").permitAll()
                )
                .csrf().disable()
                .httpBasic(withDefaults());
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