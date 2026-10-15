package com.example.taskmanagement.Services.securityService;

import com.example.taskmanagement.Dto.AuthResponse;
import com.example.taskmanagement.Dto.LoginRequest;
import com.example.taskmanagement.Dto.RegisterRequest;
import com.example.taskmanagement.Entity.AppUser;
import com.example.taskmanagement.Entity.UserRole;
import com.example.taskmanagement.Repo.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final AppUserRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenGenerator tokenGenerator;

    public AuthService(AppUserRepository repo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenGenerator tokenGenerator) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenGenerator = tokenGenerator;
    }


    public void register(RegisterRequest request) {

        String email = request.email().trim().toLowerCase();

        if (repo.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email is already registered"
            );
        }

        AppUser user = new AppUser();
        user.setName(request.name().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.password()));

        // Never accept ADMIN role from public registration
        user.setRole(UserRole.USER);
        user.setEnabled(true);

        repo.save(user);
    }

    // Authenticate user and generate JWT
    public AuthResponse login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.email().trim(),
                                request.password()
                        )
                );

        String token = tokenGenerator.generateToken(authentication);

        return new AuthResponse(token, "Bearer");


    }
}
