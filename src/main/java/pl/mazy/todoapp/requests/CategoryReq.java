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
        cat.setOwnerId(Integer.parseInt(jwtService.extractID(request).toString()));
        cR.save(cat);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@NonNull HttpServletRequest request, @PathVariable Integer id){
        if (cR.findCategoryById(id).getOwnerId().equals(jwtService.extractID(request))){
            eR.deleteAllByCategory_id(id);
            cR.deleteById(id);
        }
    }

}
