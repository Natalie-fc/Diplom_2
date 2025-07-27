package stellarburgers.model;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String email;
    private String password;
    private String name;

    private static final Faker faker = new Faker();

    public static User withoutEmail() {
        User user = new User();
        user.setPassword(faker.internet().password());
        user.setName(faker.name().firstName());
        return user;
    }

    public static User withoutPassword() {
        User user = new User();
        user.setEmail(faker.internet().emailAddress());
        user.setName(faker.name().firstName());
        return user;
    }

    public static User withoutName() {
        User user = new User();
        user.setEmail(faker.internet().emailAddress());
        user.setPassword(faker.internet().password());
        return user;
    }
}
