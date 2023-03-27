package pl.mazy.todoapp.requests;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    record NewEventRequest(

    ){

    }

    public void addEvent(){

    }
}
