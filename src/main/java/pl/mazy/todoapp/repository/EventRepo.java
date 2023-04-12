package pl.mazy.todoapp.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.mazy.todoapp.model.Event;

import java.util.List;

public interface EventRepo extends JpaRepository<Event,Integer> {

    ///select
    @Query("select e from events e  where e.dateStart >= ?2 and e.dateEnd >= ?2 and e.owner_id = ?1 order by e.dateStart,e.timeStart")
    List<Event> findEventsBetweenDates(Integer oId,String date);

    @Query("select e from events e where e.owner_id = ?1 and e.mainTask_id = ?2")
    List<Event> findEventsByMainID(Integer oId, Integer mId);

    @Query("select e from events e where e.id = ?1")
    Event findEventById(Integer id);

    @Query("select e.name from events e where e.owner_id = ?1 and  e.mainTask_id = ?2")
    List<String> findNamesByMainId(Integer oId,Integer id);

    @Query("select e from events e where e.id = ?1 and e.type = true")
    Event findTaskById(Integer id);
    @Query("select e from events e where e.category_id =?1 and e.type = true")
    List<Event> findTaskEByCategory(Integer cat);

    //Update
    @Transactional
    @Modifying
    @Query("update events set checked = ?1 where id = ?2")
    void toggleState(Boolean state,Integer id);

    @Transactional
    @Modifying
    @Query("update events set checked = false where id = ?1")
    void changeStateFalse(Integer id);

    @Transactional
    @Modifying
    @Query("update events set checked = true where mainTask_id = ?1")
    void changeStateTrue(Integer id);

    //delete
    @Modifying
    @Query("delete events where category_id = ?1")
    void deleteAllByCategory_id(Integer id);

    @Modifying
    @Query("delete events where mainTask_id = ?1")
    void deleteByMainTask_id(Integer id);
}
