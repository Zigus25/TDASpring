package pl.mazy.todoapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Entity(name = "notes")
public class Note {
    @Id
    @GeneratedValue
    private Integer id;
    @NonNull
    private Integer owner_id;
    @NonNull
    private String name;
    private String description;
}
