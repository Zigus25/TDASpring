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
            Integer id,
            String name,
            String description
    ){ }

    @PostMapping
    public void addNote(@NonNull HttpServletRequest request,@RequestBody newNoteReq req){
        Note nt;
        if (jwtService.extractID(request).equals(nR.findById(req.id).orElseThrow().getOwner_id())&&req.id!=null){
            nt = nR.findById(req.id).orElseThrow();
        } else {
            nt = new Note();
            nt.setOwner_id(jwtService.extractID(request));
        }
        nt.setName(req.name);
        nt.setDescription(req.description);
        nR.save(nt);
    }

    @DeleteMapping("{noteId}")
    public void deleteEvent(@NonNull HttpServletRequest request,@PathVariable("noteId")Integer nId){
        if (nR.findById(nId).orElseThrow().getOwner_id().equals(jwtService.extractID(request))) {
            nR.deleteById(nId);
        }
    }
}
