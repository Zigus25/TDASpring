package pl.mazy.todoapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mazy.todoapp.model.Category;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category,Integer> {
    List<Category> findCategoriesByOwnerId(Integer ownerId);

    Category findCategoryById(Integer id);

    Category findCategoryByShareIdAndOwnerId(Integer id,Integer owner);
}
