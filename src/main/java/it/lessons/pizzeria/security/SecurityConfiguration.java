package it.lessons.pizzeria.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                (authorizeHttpRequests) -> authorizeHttpRequests.requestMatchers("/user").hasAuthority("USER")
                        .requestMatchers("/admin").hasAuthority("ADMIN")
                        .requestMatchers("/").permitAll())
                .formLogin((formLogin) -> formLogin
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginPage("/authentication/login")
                        .failureUrl("/authentication/login?failed")
                        .loginProcessingUrl("/authentication/login/process"))
                .logout((logout) -> logout
                        .logoutUrl("/authentication/logout")
                        .logoutSuccessUrl("/") // URL di reindirizzamento dopo il logout
                        .invalidateHttpSession(true) // Invalida la sessione HTTP
                        .deleteCookies("JSESSIONID") // Cancella i cookie di sessione
                );

        // http.authorizeHttpRequests()
        // .requestMatchers("/user").hasAuthority("USER")
        // .requestMatchers("/admin").hasAuthority("ADMIN")
        // .requestMatchers("/").permitAll()
        // .and().formLogin()
        // .and().logout();

        return http.build();
    }

    @Bean
    DatabaseUserDetailsService userDetailsService() {
        return new DatabaseUserDetailsService();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;

    }

}