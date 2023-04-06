package pl.mazy.todoapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.mazy.todoapp.model.Event;

import java.util.List;

public interface EventRepo extends JpaRepository<Event,Integer> {
    @Query("select e from events e where e.owner_id = ?1 and e.category_id =?2 and e.type = false")
    List<Event> findTaskEBy_oID_Category(Long oID, Long cat);
}
