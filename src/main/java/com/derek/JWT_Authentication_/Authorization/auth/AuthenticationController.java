package com.derek.JWT_Authentication_.Authorization.auth;

import com.derek.JWT_Authentication_.Authorization.DTO.AuthenticationRequest;
import com.derek.JWT_Authentication_.Authorization.DTO.AuthenticationResponse;
import com.derek.JWT_Authentication_.Authorization.DTO.RegisterRequest;
import com.derek.JWT_Authentication_.Authorization.DTO.VerifyUser;
import com.derek.JWT_Authentication_.Authorization.exceptions.AccountAlreadyVerifiedException;
import com.derek.JWT_Authentication_.Authorization.exceptions.InvalidVerificationCodeException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.login(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@Valid @RequestBody VerifyUser verifyUser){
        try{
            authenticationService.verifyUser(verifyUser);
            return ResponseEntity.ok("Account verified successfully");
        } catch (InvalidVerificationCodeException | UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email){
        try{
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification Code sent");
        } catch (AccountAlreadyVerifiedException | UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
