package pl.mazy.todoapp.requests;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.mazy.todoapp.model.Note;
import pl.mazy.todoapp.repository.NoteRepo;
import pl.mazy.todoapp.services.JwtService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notes")
public class NoteReq {
    private final NoteRepo nR;
    private final JwtService jwtService;

    @GetMapping
    public List<Note> getNotes(@NonNull HttpServletRequest request){
        return nR.findAllByOwner_id(jwtService.extractID(request));
    }

    record newNoteReq(
            String name,
            String description
    ){ }

    record updateNoteReq(
            Integer id,
            String name,
            String description
    ){ }

    @PostMapping
    public void addNote(@NonNull HttpServletRequest request,@RequestBody newNoteReq req){
        Note nt = new Note();
        nt.setOwner_id(jwtService.extractID(request));
        nt.setName(req.name);
        nt.setDescription(req.description);
        nR.save(nt);
    }

    @PostMapping
    public void UpdateNote(@NonNull HttpServletRequest request,@RequestBody updateNoteReq req){
        if (jwtService.extractID(request).equals(nR.findById(req.id).orElseThrow().getOwner_id())) {
            Note nt = nR.findById(req.id).orElseThrow();
            nt.setName(req.name);
            nt.setDescription(req.description);
            nR.save(nt);
        }
    }

    @DeleteMapping("{noteId}")
    public void deleteEvent(@NonNull HttpServletRequest request,@PathVariable("noteId")Integer nId){
        if (nR.findById(nId).orElseThrow().getOwner_id().equals(jwtService.extractID(request))) {
            nR.deleteById(nId);
        }
    }
}
