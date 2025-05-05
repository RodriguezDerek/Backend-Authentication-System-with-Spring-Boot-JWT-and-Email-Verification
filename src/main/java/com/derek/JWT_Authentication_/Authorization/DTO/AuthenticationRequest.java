package com.derek.JWT_Authentication_.Authorization.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NotEmpty(message = "Email address is required.")
    @Email(message = "Please provide a valid email address.")
    private String email;

    @NotEmpty(message = "Password is required.")
    private String password;

}
