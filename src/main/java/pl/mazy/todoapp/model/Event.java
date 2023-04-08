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
@Entity(name = "events")
public class Event {
    @Id
    @GeneratedValue
    private Integer id;
    @NonNull
    private Integer owner_id;
    @NonNull
    private String name;
    private String description;
    @NonNull
    private Integer category_id;
    private String timeStart;
    private String timeEnd;
    private String dateStart;
    private String dateEnd;
    @NonNull
    private boolean type;
    @NonNull
    private boolean checked;
    @NonNull
    private String color;
    private Integer mainTask_id;
}
