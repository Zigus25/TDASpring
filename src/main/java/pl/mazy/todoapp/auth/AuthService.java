package pl.mazy.todoapp.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.mazy.todoapp.enums.Role;
import pl.mazy.todoapp.model.User;
import pl.mazy.todoapp.repository.UserRepo;
import pl.mazy.todoapp.services.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepo repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthResponse register(RegisterRequest request){
        var user = User.builder()
                .name(request.getName())
                .eMail(request.getEmail())
                .passwd(passwordEncoder.encode(request.getPasswd()))
                .role(Role.USER)
                .build();
        repo.save(user);
        var token =jwtService.generateToken(user);
        return AuthResponse.builder().token(token).build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPasswd())
        );
        var user = repo.findByEMail(request.getEmail()).orElseThrow();
        var token =jwtService.generateToken(user);
        return AuthResponse.builder().token(token).build();
    }
}
