package test.jet.game.application.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import test.jet.game.application.services.UserService;
import test.jet.game.infrastructure.persistence.entities.Role;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private UserService userService;

    @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         return http.csrf(AbstractHttpConfigurer::disable)
                 .cors(AbstractHttpConfigurer::disable)
                 .authorizeHttpRequests(
                         request ->
                                 request
                                 .requestMatchers("/swagger-ui/*", "/api-docs/*", "/api-docs").permitAll()
                                 .requestMatchers("/api/v1/game/join").permitAll()
                                 .requestMatchers("/api/v1/game/make-move").hasAnyAuthority(Role.USER.name())
                                 .requestMatchers("/api/v1/game/subscribe").hasAnyAuthority(Role.USER.name())
                                 .anyRequest()
                                 .authenticated()
                 )
                 .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 .authenticationProvider(authenticationProvider()).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                 .build();
     }

     @Bean
     public AuthenticationProvider authenticationProvider() {
         DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
         daoAuthenticationProvider.setUserDetailsService(userService.userDetailsService());
         daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
         return daoAuthenticationProvider;
     }

     @Bean
     public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
     }

     @Bean
     public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
     }
}
