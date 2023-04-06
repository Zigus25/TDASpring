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
@Entity(name = "events")
public class Event {
    @Id
    @GeneratedValue
    private Long id;
    private Long owner_id;
    private String name;
    private String description;
    private Long category_id;
    private String timeStart;
    private String timeEnd;
    private String dateStart;
    private String dateEnd;
    private boolean type;
    private boolean checked;
    private String color;
    private int mainTask_id;
}
