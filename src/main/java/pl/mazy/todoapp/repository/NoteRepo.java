package pl.mazy.todoapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.mazy.todoapp.model.Note;

import java.util.List;

public interface NoteRepo extends JpaRepository<Note,Integer> {
    @Query("select n from notes n where n.owner_id = ?1")
    List<Note> findAllByOwner_id(Integer oId);
}
