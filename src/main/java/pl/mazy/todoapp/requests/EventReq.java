package pl.mazy.todoapp.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.mazy.todoapp.model.Event;
import pl.mazy.todoapp.repository.EventRepo;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventReq {
    private final EventRepo eR;

    @GetMapping("/{oId}/{category}")
    public List<Event> getEvents(@PathVariable("oId")Long oId,@PathVariable("category") Long category){
        return eR.findTaskEBy_oID_Category(oId,category);
    }

    record NewEventRequest(
            Long owner_id,
            String name,
            String description,
            Long category_id,
            String timeStart,
            String timeEnd,
            String dateStart,
            String dateEnd,
            Boolean type,
            Boolean checked,
            String color,
            Integer mainTask_id
    ){ }

    @PostMapping
    public void addEvent(@RequestBody NewEventRequest req){
        Event ev = new Event();
        ev.setOwner_id(req.owner_id);
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
    public void deleteEvent(@PathVariable("eventId")Integer eId){
        eR.deleteById(eId);
    }
}
