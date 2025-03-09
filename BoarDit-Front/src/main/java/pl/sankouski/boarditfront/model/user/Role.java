package pl.sankouski.boarditfront.model.user;


import java.util.ArrayList;
import java.util.List;


public class Role {


    private String pid;


    private String description;


    private List<User> users = new ArrayList<>();

    public Role() {
    }

    public Role(final String name, final String description) {
        this.pid = name;
        this.description = description;
    }


    public String getName() {
        return pid;
    }

    public List<User> getUsers() {
        return users;
    }

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
}
