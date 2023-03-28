package pl.mazy.todoapp.requests;

import org.springframework.web.bind.annotation.*;
import pl.mazy.todoapp.model.Note;
import pl.mazy.todoapp.repository.NoteRepo;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteReq {
    private final NoteRepo nR;

    public NoteReq(NoteRepo nR) {
        this.nR = nR;
    }

    @GetMapping
    public List<Note> getNotes(){
        return nR.findAll();
    }

    record newNoteReq(
            Integer owner_id,
            String name,
            String description
    ){ }

    @PostMapping
    public void addNote(@RequestBody newNoteReq req){
        Note nt = new Note();
        nt.setOwner_id(req.owner_id);
        nt.setName(req.name);
        nt.setDescription(req.description);
        nR.save(nt);
    }

    @DeleteMapping("{noteId}")
    public void deleteEvent(@PathVariable("noteId")Integer nId){
        nR.deleteById(nId);
    }
}
