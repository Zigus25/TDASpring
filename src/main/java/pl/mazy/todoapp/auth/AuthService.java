package pl.mazy.todoapp.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.mazy.todoapp.enums.Role;
import pl.mazy.todoapp.model.User;
import pl.mazy.todoapp.repository.UserRepo;
import pl.mazy.todoapp.services.JwtService;
import pl.mazy.todoapp.token.Token;
import pl.mazy.todoapp.token.TokenRepo;
import pl.mazy.todoapp.token.TokenType;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepo repo;
    private final TokenRepo tR;
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
        return AuthResponse.builder().accessToken(token).build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPasswd())
        );
        var user = repo.findByEMail(request.getEmail()).orElseThrow();
        var token =jwtService.generateToken(user);
        return AuthResponse.builder().accessToken(token).build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tR.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tR.findAllValidTokenByUser(Integer.parseInt(user.getId().toString()));
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tR.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final int id;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        id = Integer.parseInt(jwtService.extractUsername(refreshToken));
        var user = this.repo.findUserById(id)
                .orElseThrow();
        if (jwtService.isValid(refreshToken, user)) {
            var accessToken = jwtService.generateToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, accessToken);
            var authResponse = AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
        }
    }
}
