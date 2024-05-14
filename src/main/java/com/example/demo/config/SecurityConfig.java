package com.example.demo.config;

import com.example.demo.handler.CustomLoginSuccessHandler;
import com.example.demo.security.CustomEmployeeDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomEmployeeDetailService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomLoginSuccessHandler();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/login").permitAll();
                    registry.requestMatchers("/manager/**").hasAuthority("MANAGER");
                    registry.requestMatchers("/builder/**").hasAuthority("BUILDER");
                    registry.anyRequest().authenticated();
                })
                .formLogin(login ->
                        login.successHandler(authenticationSuccessHandler()).permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout") // Customize logout URL if needed
                        .logoutSuccessUrl("/login?logout") // Redirect to login page after logout
                        .invalidateHttpSession(true) // Invalidate HTTP session on logout
                        .deleteCookies("JSESSIONID") // Delete session cookie on logout
                )
                .build();
    }
}
