package com.example.serverapi.dto.auth_user;

import com.example.serverapi.annotation.PasswordValidatorPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthAndRegRequest {
    @NotNull
    @NotBlank
    @Pattern(regexp = "^([a-zA-Z]{4,}([!@#$%^&*()_+.=]*|\\d*)*)${4,50}")
    private String username;
    @NotNull
    @NotBlank
    @PasswordValidatorPattern(message = "password must contains only one @ character," +
                                        " minimum four ascii upper and lower letters," +
                                        " minimum one digit")
    private String password;
}
