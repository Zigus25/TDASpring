package pl.mazy.todoapp.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.mazy.todoapp.enums.Role;
import pl.mazy.todoapp.model.Category;
import pl.mazy.todoapp.model.User;
import pl.mazy.todoapp.repository.CategoryRepo;
import pl.mazy.todoapp.repository.UserRepo;
import pl.mazy.todoapp.services.JwtService;
import pl.mazy.todoapp.token.Token;
import pl.mazy.todoapp.token.TokenRepo;
import pl.mazy.todoapp.token.TokenType;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepo repo;
    private final TokenRepo tR;
    private final CategoryRepo cR;
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
        var sUser = repo.save(user);
        Category cat = new Category();
        cat.setOwnerId(sUser.getId());
        cat.setName("Main");
        cR.save(cat);
        var token =jwtService.generateToken(user);
        saveUserToken(sUser,token);
        return AuthResponse.builder().accessToken(token).build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPasswd())
        );
        var user = repo.findByEMail(request.getEmail()).orElseThrow();
        var token =jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user,token);
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
        var validUserTokens = tR.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tR.saveAll(validUserTokens);
    }
}
