package pl.mazy.todoapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "notes")
public class Note {
    @Id
    @GeneratedValue
    private Long id;
    private Long owner_id;
    private String name;
    private String description;
}
