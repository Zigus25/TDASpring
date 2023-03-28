package pl.mazy.todoapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mazy.todoapp.model.Note;

public interface NoteRepo extends JpaRepository<Note,Integer> {
}
