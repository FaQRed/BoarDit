package pl.sankouski.boarditfront.service;

import org.junit.jupiter.api.Test;
import pl.sankouski.boarditfront.model.user.Role;

import static org.assertj.core.api.Assertions.assertThat;

class StringToRoleConverterTest {

    private final StringToRoleConverter converter = new StringToRoleConverter();

    @Test
    void testConvertValidString() {
        String source = "ROLE_ADMIN";
        Role result = converter.convert(source);

        assertThat(result).isNotNull();
        assertThat(result.getPid()).isEqualTo(source);
    }

    @Test
    void testConvertEmptyString() {
        String source = "";
        Role result = converter.convert(source);

        assertThat(result).isNotNull();
        assertThat(result.getPid()).isEqualTo(source);
    }

    @Test
    void testConvertNullString() {
        String source = null;
        Role result = converter.convert(source);

        assertThat(result).isNotNull();
        assertThat(result.getPid()).isNull();
    }
}