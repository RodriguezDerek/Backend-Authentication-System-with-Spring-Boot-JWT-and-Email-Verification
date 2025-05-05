package com.derek.JWT_Authentication_.Authorization.auth;

import com.derek.JWT_Authentication_.Authorization.DTO.AuthenticationRequest;
import com.derek.JWT_Authentication_.Authorization.DTO.AuthenticationResponse;
import com.derek.JWT_Authentication_.Authorization.DTO.RegisterRequest;
import com.derek.JWT_Authentication_.Authorization.DTO.VerifyUser;
import com.derek.JWT_Authentication_.Authorization.exceptions.*;
import com.derek.JWT_Authentication_.Authorization.jwt.JwtService;
import com.derek.JWT_Authentication_.Authorization.model.Role;
import com.derek.JWT_Authentication_.Authorization.model.User;
import com.derek.JWT_Authentication_.Authorization.repository.UserRepository;
import com.derek.JWT_Authentication_.Authorization.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthenticationResponse register(@Valid RegisterRequest request) {
        Optional<User> optionalUser = repository.findByEmail(request.getEmail());

        if(optionalUser.isEmpty()){
            User user = User
                    .builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .build();

            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
            user.setEnabled(false);
            sendVerificationEmail(user);

            repository.save(user);
            String jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse
                    .builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .jwtToken(jwtToken)
                    .isEnabled(user.isEnabled())
                    .build();
        }

        throw new UsernameAlreadyExistsException("Email already exists");
    }

    public AuthenticationResponse login(@Valid AuthenticationRequest request) {
        User user = repository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.isEnabled()) {
            throw new AccountNotVerifiedException("Please verify your account by clicking the verification link sent to your email.");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new InvalidLoginException("Invalid email or password");
        }

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse
                .builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .jwtToken(jwtToken)
                .isEnabled(user.isEnabled())
                .build();
    }

    public void verifyUser(VerifyUser verifyUser){
        Optional<User> optionalUser = repository.findByEmail(verifyUser.getEmail());

        if(optionalUser.isPresent()){
            User user = optionalUser.get();

            if(user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())){
                throw new VerificationCodeExpiredException("Verification Code is expired");
            }

            if(user.getVerificationCode().equals(verifyUser.getVerificationCode())){
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                repository.save(user);
            } else{
                throw new InvalidVerificationCodeException("Invalid verification code");
            }

        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public void resendVerificationCode(String email){
        Optional<User> optionalUser = repository.findByEmail(email);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            if(user.isEnabled()){
                throw new AccountAlreadyVerifiedException("Account is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
            sendVerificationEmail(user);
            repository.save(user);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public void sendVerificationEmail(User user){
        String subject = "Account Verification";
        String verificationCode = user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e){
            e.printStackTrace();
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

}
