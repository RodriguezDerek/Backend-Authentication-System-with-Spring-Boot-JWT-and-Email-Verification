package com.derek.JWT_Authentication_.Authorization.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String jwtToken;
    private boolean isEnabled;
}
