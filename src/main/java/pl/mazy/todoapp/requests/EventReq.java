package pl.mazy.todoapp.requests;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.mazy.todoapp.model.Event;
import pl.mazy.todoapp.repository.EventRepo;
import pl.mazy.todoapp.services.JwtService;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventReq {
    private final EventRepo eR;
    private final JwtService jwtService;

    record NewEventRequest(
            Integer id,
            String name,
            String description,
            Integer category_id,
            String timeStart,
            String timeEnd,
            String dateStart,
            String dateEnd,
            Boolean type,
            Boolean checked,
            String color,
            Integer mainTask_id
    ){ }

    //tasks
    @GetMapping("/{category}")
    public List<Event> getTasksInCategory(@PathVariable Integer category){
        return eR.findTaskEByCategory(category);
    }

    @GetMapping("/i={id}")
    public Event getEventByID(@PathVariable Integer id){
        return eR.findTaskById(id);
    }

    @PostMapping("/t={id}")
    public void toggleTask(@NonNull HttpServletRequest request, @PathVariable Integer id){
        var ev = eR.findEventById(id);
        if (ev.getOwner_id().equals(jwtService.extractID(request))) {
            eR.toggleState(!ev.isChecked(),id);
        }
    }

    @PostMapping("/f={id}")
    public void changeFalse(@NonNull HttpServletRequest request, @PathVariable Integer id){
        if (eR.findTaskById(id).getOwner_id().equals(jwtService.extractID(request))) {
            eR.changeStateFalse(id);
        }
    }

    @PostMapping("/tr={id}")
    public void changeTrue(@NonNull HttpServletRequest request, @PathVariable Integer id){
        if (eR.findTaskById(id).getOwner_id().equals(jwtService.extractID(request))) {
            eR.changeStateTrue(id);
        }
    }

    //events
    @GetMapping("/d={date}")
    public List<Event> getBetween(@NonNull HttpServletRequest request, @PathVariable String date){
        return eR.findEventsBetweenDates(jwtService.extractID(request),date);
    }

    @GetMapping("/m={mId}")
    public List<Event> getInEvent(@NonNull HttpServletRequest request, @PathVariable Integer mId){
        return eR.findEventsByMainID(jwtService.extractID(request),mId);
    }

    @GetMapping("/nM={mId}")
    public List<String> getNamesEvent(@NonNull HttpServletRequest request, @PathVariable Integer mId){
        return eR.findNamesByMainId(jwtService.extractID(request),mId);
    }

    //both
    @PostMapping
    public void addEvent(@NonNull HttpServletRequest request,@RequestBody NewEventRequest req){
        Event ev;
        if (jwtService.extractID(request).equals(eR.findEventById(req.id).getOwner_id())&&req.id!=null){
            ev = eR.findEventById(req.id);
        }else {
            ev = new Event();
            ev.setOwner_id(jwtService.extractID(request));
        }
        ev.setName(req.name);
        ev.setDescription(req.description);
        ev.setCategory_id(req.category_id);
        ev.setTimeStart(req.timeStart);
        ev.setTimeEnd(req.timeEnd);
        ev.setDateEnd(req.dateEnd);
        ev.setDateStart(req.dateStart);
        ev.setType(req.type);
        ev.setChecked(req.checked);
        ev.setColor(req.color);
        ev.setMainTask_id(req.mainTask_id);
        eR.save(ev);
    }

    @DeleteMapping("{eventId}")
    public void deleteEvent(@NonNull HttpServletRequest request,@PathVariable("eventId")Integer eId){
        if (eR.findEventById(eId).getOwner_id().equals(jwtService.extractID(request))) {
            eR.deleteByMainTask_id(eId);
            eR.deleteById(eId);
        }
    }
}
