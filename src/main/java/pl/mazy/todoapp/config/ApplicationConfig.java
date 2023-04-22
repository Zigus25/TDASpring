package pl.mazy.todoapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.mazy.todoapp.repository.UserRepo;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final UserRepo repo;
    @Bean
    public UserDetailsService userDetailsService(){
        return id -> repo.findUserById(Integer.parseInt(id)).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public UserDetailsService userDetailsServiceMail(){
        return mail -> repo.findByEMail(mail).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsServiceMail());
        authProvider.setPasswordEncoder(passwdEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwdEncoder() {
        return new BCryptPasswordEncoder();
    }
}
