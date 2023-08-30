package pl.mazy.todoapp.requests;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.mazy.todoapp.model.Category;
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

    private List<Events> toEvents(List<Event> events){
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
    }

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
            Category cat = cR.findCategoryById(category);
            Integer cid;
            if (cat.getShareId()==null){cid=cat.getId();}else{cid=cat.getShareId();}
            return consolidate(convert(eR.findTaskEByCategory(cid)));
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
        Integer id = jwtService.extractID(request);
        Category ccr = cR.findCategoryByShareIdAndOwnerId(ev.getCategory_id(),id);
        int coid= -1;
        if(ccr!=null){coid = ccr.getOwnerId();}
        Category cr = cR.findCategoryById(ev.getCategory_id());
        int oid= -1;
        if(cr!=null){oid = cr.getOwnerId();}
        if (ev.getOwner_id().equals(id)||coid ==id||oid==id) {
            eR.toggleState(!ev.isChecked(),ev.getId());
            if (!ev.getSubList().isEmpty()){
                toggleCheckSub(ev);
            }
            checkBack(ev.getMainTask_id());
        }
    }

    @PostMapping("/uA")
    public void unmarkAll(@NonNull HttpServletRequest request, @RequestBody Events ev){
        Integer id = jwtService.extractID(request);
        Category ccr = cR.findCategoryByShareIdAndOwnerId(ev.getCategory_id(),id);
        int coid= -1;
        if(ccr!=null){coid = ccr.getOwnerId();}
        Category cr = cR.findCategoryById(ev.getCategory_id());
        int oid= -1;
        if(cr!=null){oid = cr.getOwnerId();}
        if (ev.getOwner_id().equals(id)||coid ==id||oid==id) {
            falseCheckSub(ev);
        }

    }

    //events
    @GetMapping("/d={date}")
    public List<Events> getBetween(@NonNull HttpServletRequest request, @PathVariable String date){
        return convert(eR.findEventsBetweenDates(jwtService.extractID(request),date));
    }

    //both
    @PostMapping
    public void addEvent(@NonNull HttpServletRequest request,@RequestBody NewEventRequest req){
        var id = jwtService.extractID(request);
        List<String> sublistL;
        if (cR.findCategoryById(req.category_id).getOwnerId().equals(id)||cR.findCategoryByShareIdAndOwnerId(req.category_id,id).getOwnerId().equals(id)) {
            Event ev;
            if (req.id != null){
                sublistL = new ArrayList<>(eR.findNamesByMainId(req.id));
                ev = eR.findEventById(req.id);
                ev.setChecked(false);
                checkBack(req.mainTask_id);
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
            if (sublistL.size()<req.subList.size()) {
                for (int i = sublistL.size(); i < req.subList.size(); i++) {
                    addSubEvent(req.subList.get(i), evs.getCategory_id(), evs.getColor(), evs.getId(), id);
                }
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
        Event e = eR.findEventById(eId);
        Integer id = jwtService.extractID(request);
        Category cr = cR.findCategoryById(e.getCategory_id());
        int coid = -1;
        if(cr!=null){coid = cr.getOwnerId();}
        Category cSd = cR.findCategoryByShareIdAndOwnerId(e.getCategory_id(),id);
        int csdi = -1;
        if (cSd!=null){
            csdi = cSd.getOwnerId();
        }
        System.out.println(e+" "+ id+" "+coid+" "+cr+" "+csdi+" "+cSd);
        if (e.getOwner_id().equals(id)||csdi==id||coid==id) {
            var ev = eR.findEventsByMainID(eId);
            for (Event eve: ev) {
                deleteEvent(request,eve.getId());
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

    private List<Events> convert(List<Event> events){
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
        return list;
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

    private void falseCheckSub(Events ev){
        eR.changeStateFalse(ev.getId());
        if (!ev.getSubList().isEmpty()){
            for (Events e : ev.getSubList()){
                falseCheckSub(e);
            }
        }
    }
}
