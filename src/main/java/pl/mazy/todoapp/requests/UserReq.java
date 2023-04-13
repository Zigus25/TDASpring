package pl.mazy.todoapp.requests;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mazy.todoapp.model.User;
import pl.mazy.todoapp.repository.UserRepo;
import pl.mazy.todoapp.services.JwtService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserReq {
    private final UserRepo uR;
    private final JwtService jwtService;

    record IDReq(String email){}

    @PostMapping
    public User getID(@NonNull HttpServletRequest request, @RequestBody IDReq req){
        var a = uR.findByEMail(req.email).orElseThrow();
        if (a.getId().equals(jwtService.extractID(request))){
            return a;
        }else {
            return null;
        }
    }
}
