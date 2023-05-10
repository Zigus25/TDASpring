package pl.mazy.todoapp.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.List;

@Data
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Events {
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
    private boolean type;
    private boolean checked;
    @NonNull
    private String color;
    private Integer mainTask_id;
    private List<Events> subList;
}