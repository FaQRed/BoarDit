package pl.sankouski.boarditfront.service;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.sankouski.boarditfront.model.user.Role;

@Component
public class StringToRoleConverter implements Converter<String, Role> {

    @Override
    public Role convert(String source) {
        Role role = new Role();
        role.setPid(source);
        return role;
    }
}