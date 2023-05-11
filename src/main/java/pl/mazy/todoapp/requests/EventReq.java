package pl.mazy.todoapp.requests;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.mazy.todoapp.model.Event;
import pl.mazy.todoapp.model.Events;
import pl.mazy.todoapp.repository.CategoryRepo;
import pl.mazy.todoapp.repository.EventRepo;
import pl.mazy.todoapp.services.JwtService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventReq {
    private final EventRepo eR;
    private final CategoryRepo cR;
    private final JwtService jwtService;

    record NewEventRequest(
            @Nullable Integer id,
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
            Integer mainTask_id,
            List<String> subList
    ){ }

    //tasks
    @GetMapping("/{category}")
    public List<Events> getTasksInCategory(@NonNull HttpServletRequest request, @PathVariable Integer category) {
        if (jwtService.extractID(request).equals(cR.findCategoryById(category).getOwnerId())) {
            List<Event> events = eR.findTaskEByCategory(category);
            List<Events> list = new ArrayList<>();
            for (Event e: events) {
                list.add(new Events(
                    e.getId(),
                    e.getOwner_id(),
                    e.getName(),
                    e.getDescription(),
                    e.getCategory_id(),
                    e.getTimeStart(),
                    e.getTimeEnd(),
                    e.getDateStart(),
                    e.getDateEnd(),
                    e.isType(),
                    e.isChecked(),
                    e.getColor(),
                    e.getMainTask_id(),
                    new ArrayList<>()
                ));
            }
            return consolidate(list);
        } else{
            return null;
        }
    }

    @GetMapping("/i={id}")
    public Event getEventByID(@NonNull HttpServletRequest request, @PathVariable Integer id){
        if (eR.findTaskById(id).getOwner_id().equals(jwtService.extractID(request))){
            return eR.findTaskById(id);
        } else {
            return null;
        }
    }

    @PostMapping("/t")
    public void toggleTask(@NonNull HttpServletRequest request, @RequestBody Events ev){
        if (ev.getOwner_id().equals(jwtService.extractID(request))) {
            eR.toggleState(!ev.isChecked(),ev.getId());
            if (!ev.getSubList().isEmpty()){
                toggleCheckSub(ev);
            }
            checkBack(ev.getMainTask_id());
        }
    }

    //events
    @GetMapping("/d={date}")
    public List<Event> getBetween(@NonNull HttpServletRequest request, @PathVariable String date){
        return eR.findEventsBetweenDates(jwtService.extractID(request),date);
    }

    //both
    @PostMapping
    public void addEvent(@NonNull HttpServletRequest request,@RequestBody NewEventRequest req){
        var id = jwtService.extractID(request);
        List<String> sublistL;
        if (cR.findCategoryById(req.category_id).getOwnerId().equals(id)) {
            Event ev;
            if (req.id != null){
                sublistL = new ArrayList<>(eR.findNamesByMainId(jwtService.extractID(request),req.id));
                if (id.equals(eR.findEventById(req.id).getOwner_id())) {
                    ev = eR.findEventById(req.id);
                    ev.setChecked(false);
                    checkBack(req.mainTask_id);
                } else {
                    return;
                }
            }else {
                ev = new Event();
                sublistL = new ArrayList<>();
                ev.setOwner_id(jwtService.extractID(request));
                ev.setChecked(req.checked);
            }
            ev.setName(req.name);
            ev.setDescription(req.description);
            ev.setCategory_id(req.category_id);
            ev.setTimeStart(req.timeStart);
            ev.setTimeEnd(req.timeEnd);
            ev.setDateEnd(req.dateEnd);
            ev.setDateStart(req.dateStart);
            ev.setType(req.type);
            ev.setColor(req.color);
            ev.setMainTask_id(req.mainTask_id);
            var evs = eR.save(ev);
            for (int i = sublistL.size();i<req.subList.size();i++){
                addSubEvent(req.subList.get(i),evs.getCategory_id(),evs.getColor(),evs.getId(),id);
            }
        }
    }

    private void checkBack(Integer mainTaskId) {
        if (mainTaskId!=null){
            var ev = eR.findTaskById(mainTaskId);
            ev.setChecked(false);
            eR.save(ev);
            checkBack(ev.getMainTask_id());
        }
    }

    @DeleteMapping("{eventId}")
    public void deleteEvent(@NonNull HttpServletRequest request,@PathVariable("eventId")Integer eId){
        if (eR.findEventById(eId).getOwner_id().equals(jwtService.extractID(request))) {
            var ev = eR.findEventsByMainID(jwtService.extractID(request),eId);
            for (Event e: ev) {
                deleteEvent(request,e.getId());
            }
            eR.deleteById(eId);
        }
    }

    private List<Events> consolidate(List<Events> lisEv){

        List<Events> list = new ArrayList<>(lisEv);
        for (int i = list.size()-1;i>=0;i--) {
            Events e = list.get(i);
            if (e.getMainTask_id() != null) {
                Events j = list.stream().filter(x -> Objects.equals(x.getId(), e.getMainTask_id())).findFirst().get();
                List<Events> sE = new ArrayList<>(j.getSubList());
                sE.add(e);
                j.setSubList(sE);
                list.set(list.indexOf(j),j);
            }
        }
        return list.stream().filter( e->e.getMainTask_id() == null ).toList();
    }

    private void addSubEvent(String name,Integer cat,String color, Integer id, Integer oId){
        Event ev = new Event();
        ev.setName(name);
        ev.setOwner_id(oId);
        ev.setDescription(null);
        ev.setCategory_id(cat);
        ev.setTimeStart(null);
        ev.setTimeEnd(null);
        ev.setDateEnd(null);
        ev.setDateStart(null);
        ev.setType(true);
        ev.setChecked(false);
        ev.setColor(color);
        ev.setMainTask_id(id);
        eR.save(ev);
    }

    private void toggleCheckSub(Events ev){
        eR.changeStateTrue(ev.getId());
        if (!ev.getSubList().isEmpty()){
            for (Events e : ev.getSubList()){
                toggleCheckSub(e);
            }
        }
    }
}
