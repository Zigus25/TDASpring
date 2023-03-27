package pl.mazy.todoapp.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Event {
    @Id
    @SequenceGenerator(
            name = "event_id_seq",
            sequenceName = "event_id_seq"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "event_id_seq"
    )
    private int id;
    private  int owner_id;
    private String name;
    private String description;
    private int category_id;
    private String timeStart;
    private String timeEnd;
    private String dateStart;
    private String dateEnd;
    private boolean type;
    private boolean checked;
    private String color;
    private int mainTask_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getMainTask_id() {
        return mainTask_id;
    }

    public void setMainTask_id(int mainTask_id) {
        this.mainTask_id = mainTask_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id && owner_id == event.owner_id && category_id == event.category_id && type == event.type && checked == event.checked && mainTask_id == event.mainTask_id && Objects.equals(name, event.name) && Objects.equals(description, event.description) && Objects.equals(timeStart, event.timeStart) && Objects.equals(timeEnd, event.timeEnd) && Objects.equals(dateStart, event.dateStart) && Objects.equals(dateEnd, event.dateEnd) && Objects.equals(color, event.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner_id, name, description, category_id, timeStart, timeEnd, dateStart, dateEnd, type, checked, color, mainTask_id);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", owner_id=" + owner_id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category_id=" + category_id +
                ", timeStart='" + timeStart + '\'' +
                ", timeEnd='" + timeEnd + '\'' +
                ", dateStart='" + dateStart + '\'' +
                ", dateEnd='" + dateEnd + '\'' +
                ", type=" + type +
                ", checked=" + checked +
                ", color='" + color + '\'' +
                ", mainTask_id=" + mainTask_id +
                '}';
    }
}
