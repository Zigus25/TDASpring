package pl.mazy.todoapp.requests;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.mazy.todoapp.model.Category;
import pl.mazy.todoapp.repository.CategoryRepo;
import pl.mazy.todoapp.repository.EventRepo;
import pl.mazy.todoapp.services.JwtService;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryReq {
    private final EventRepo eR;
    private final CategoryRepo cR;
    private final JwtService jwtService;

    @GetMapping
    public List<Category> getUserCategory(@NonNull HttpServletRequest request){
        return cR.findCategoriesByOwnerId(Integer.parseInt(jwtService.extractID(request).toString()));
    }

    @PostMapping("/{name}")
    public void  addCategory(@NonNull HttpServletRequest request, @PathVariable String name){
        Category cat = new Category();
        cat.setName(name);
        cat.setOwnerId(jwtService.extractID(request));
        cR.save(cat);
    }

    @PostMapping("/cID={cid}/sID={sid}")
    public void shareCategory(@NonNull HttpServletRequest request,@PathVariable Integer cid, @PathVariable Integer sid){
        Category cat = cR.findCategoryById(cid);
        if (jwtService.extractID(request).equals(cat.getOwnerId())) {
            Category category = new Category();
            category.setOwnerId(sid);
            category.setName(cat.getName());
            category.setShareId(cat.getId());
            cR.save(category);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@NonNull HttpServletRequest request, @PathVariable Integer id){
        if (cR.findCategoryById(id).getOwnerId().equals(jwtService.extractID(request))){
            eR.deleteAllByCategory_id(id);
            cR.deleteById(id);
        }
    }

}
