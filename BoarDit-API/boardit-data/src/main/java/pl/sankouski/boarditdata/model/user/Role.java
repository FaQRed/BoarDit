package pl.sankouski.boarditdata.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Role {

    @Id
    private String pid;

    @Column
    private String description;


    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> users = new ArrayList<>();

    public Role() {
    }

    public Role(final String name, final String description) {
        this.pid = name;
        this.description = description;
    }

    @Transient
    public String getName() {
        return pid;
    }

    @Transient
    public String getAuthority() {
        return pid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }


    @Override
    public int hashCode() {
        return Objects.hash(pid);
    }
}
