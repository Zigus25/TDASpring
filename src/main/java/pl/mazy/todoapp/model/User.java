package pl.mazy.todoapp.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class User {
    @Id
    @SequenceGenerator(
            name = "user_id_seq",
            sequenceName = "user_id_seq"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_id_seq"
    )
    private Long id;
    private String name;
    private String passwd;
    private Long aKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public Long getaKey() {
        return aKey;
    }

    public void setaKey(Long aKey) {
        this.aKey = aKey;
    }

    public User() {}

    public User(String name, String passwd, Long aKey) {
        this.name = name;
        this.passwd = passwd;
        this.aKey = aKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(passwd, user.passwd) && Objects.equals(aKey, user.aKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, passwd, aKey);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", passwd='" + passwd + '\'' +
                ", aKey=" + aKey +
                '}';
    }
}
