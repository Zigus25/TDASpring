package pl.mazy.todoapp.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mazy.todoapp.model.User;
import pl.mazy.todoapp.repository.UserRepo;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserReq {
    private final UserRepo uR;

    record IDReq(String email){}

    @PostMapping
    public User getID(@RequestBody IDReq req){
        return uR.findByEMail(req.email).get();
    }
}
