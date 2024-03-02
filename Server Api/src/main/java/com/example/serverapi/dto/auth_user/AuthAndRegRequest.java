package com.example.serverapi.dto.auth_user;

import com.example.serverapi.annotation.PasswordValidatorPattern;
import com.example.serverapi.enums.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthAndRegRequest {
    @NotNull
    @Size(min = 4, max = 50)
    @Pattern(regexp = "^([a-zA-Z]{4,}([@#$_]*|\\d*)*)$",
            message = "username length must begin with at least 4 ascii upper or lower " +
                      "letters then may continue only @#$_ characters or numbers")

    private String username;
    @NotNull
    @Size(min = 4, max = 50)
    @PasswordValidatorPattern(message = "password must contains only one @ character," +
                                        " minimum four ascii upper or lower letters," +
                                        " minimum one digit")
    private String password;

    private List<Role> roleList;
}
