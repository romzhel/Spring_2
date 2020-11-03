package ru.romzhel.eshop.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.romzhel.eshop.validation.FieldMatch;
import ru.romzhel.eshop.validation.ValidEmail;
import ru.romzhel.eshop.validation.ValidPhone;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@FieldMatch(first = "password", second = "matchingPassword", message = "Пароль должен совпадать")
public class SystemUser {
    @NotNull(message = "требуется")
    @Size(min = 3, message = "не менее 3 символов")
//    @Pattern(regexp = "^[a-zA-Z0-9]{5}", message = "only 5 letters/digits")
    private String userName;

    @NotNull(message = "требуется")
    @Size(min = 1, message = "требуется")
    private String password;

    @NotNull(message = "требуется")
    @Size(min = 1, message = "требуется")
    private String matchingPassword;

    @NotNull(message = "требуется")
    @Size(min = 1, message = "требуется")
    private String firstName;

    @NotNull(message = "требуется")
    @Size(min = 1, message = "требуется")
    private String lastName;

    @ValidEmail
    @NotNull(message = "требуется")
    @Size(min = 1, message = "требуется")
    private String email;

    @NotNull(message = "требуется")
    @ValidPhone(message = "недопустимый номер телефона")
    private String phone;

    @Override
    public String toString() {
        return "SystemUser{" +
                "userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
