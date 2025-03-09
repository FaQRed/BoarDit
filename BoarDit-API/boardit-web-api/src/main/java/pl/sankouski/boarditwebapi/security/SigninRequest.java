package pl.sankouski.boarditwebapi.security;


import lombok.Data;

@Data
public class SigninRequest {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public SigninRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public SigninRequest setPassword(String password) {
        this.password = password;
        return this;
    }
}
