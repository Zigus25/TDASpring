package pl.mazy.todoapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Entity(name = "category")
public class Category {
    @Id
    @GeneratedValue
    private Integer id;
    @NonNull
    private String name;
    @NonNull
    private Integer ownerId;
}
