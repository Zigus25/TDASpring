package pl.mazy.todoapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.mazy.todoapp.model.User;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {

    @Query("select u from users u where u.eMail = ?1")
    Optional<User> findByUsername(String name);

}
