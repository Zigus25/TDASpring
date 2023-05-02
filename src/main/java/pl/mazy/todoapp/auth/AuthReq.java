package pl.mazy.todoapp.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mazy.todoapp.repository.UserRepo;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthReq {
    private final AuthService service;
    private final UserRepo uR;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        if (uR.findByEMail(request.getEmail()).isEmpty()) {
            return ResponseEntity.ok(service.register(request));
        }else {
            return (ResponseEntity<AuthResponse>) ResponseEntity.badRequest();
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authentication (@RequestBody AuthRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }
}
