package pl.mazy.todoapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mazy.todoapp.model.Event;

public interface EventRepo extends JpaRepository<Event,Integer> {
}
