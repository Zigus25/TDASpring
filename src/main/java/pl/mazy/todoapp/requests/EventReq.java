package pl.mazy.todoapp.requests;

import org.springframework.web.bind.annotation.*;
import pl.mazy.todoapp.model.Event;
import pl.mazy.todoapp.repository.EventRepo;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventReq {


    private final EventRepo eR;

    public EventReq(EventRepo eR) {
        this.eR = eR;
    }

    @GetMapping
    public List<Event> getEvents(){
        return eR.findAll();
    }

//    @GetMapping("/id}/{cat}")
//    public List<Event> getByIC(@PathVariable("id") Long oID,@PathVariable("cat") Long cat){
//        return eR.findAllEBy_oID_Cat(oID,cat);
//    }

    record NewEventRequest(
            Long owner_id,
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
