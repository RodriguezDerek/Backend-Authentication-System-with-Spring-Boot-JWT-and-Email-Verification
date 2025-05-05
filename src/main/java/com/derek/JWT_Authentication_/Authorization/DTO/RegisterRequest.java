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
public class RegisterRequest {
    @NotEmpty(message = "First name is required.")
    private String firstName;

    @NotEmpty(message = "Last name is required.")
    private String lastName;

    @NotEmpty(message = "Email address is required.")
    @Email(message = "Please enter a valid email address (e.g., name@example.com).")
    private String email;

    @NotEmpty(message = "Password is required.")
    private String password;
}
