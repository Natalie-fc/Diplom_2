package stellarburgers.model;

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

    public static User withoutEmail() {
        User user = new User();
        user.setPassword("Sandra2025");
        user.setName("Natalya");
        return user;
    }

    public static User withoutPassword() {
        User user = new User();
        user.setEmail("sandra-bullock55@yandex.ru");
        user.setName("Vassya");
        return user;
    }

    public static User withoutName() {
        User user = new User();
        user.setEmail("sandra-bullock55@yandex.ru");
        user.setPassword("craig5000");
        return user;
    }
}
