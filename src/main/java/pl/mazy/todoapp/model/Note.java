package pl.mazy.todoapp.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Note {
    @Id
    @SequenceGenerator(
            name = "note_id_seq",
            sequenceName = "note_id_seq"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "note_id_seq"
    )
    private Integer id;
    private Integer owner_id;
    private String name;
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(Integer owner_id) {
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

    public Note() {
    }

    public Note(Integer owner_id, String name, String description) {
        this.owner_id = owner_id;
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(id, note.id) && Objects.equals(owner_id, note.owner_id) && Objects.equals(name, note.name) && Objects.equals(description, note.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner_id, name, description);
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", owner_id=" + owner_id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
