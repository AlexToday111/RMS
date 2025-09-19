package com.rms.auth;

import com.rms.security.JwtService;
import com.rms.user.Role;
import com.rms.user.User;
import com.rms.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody AuthDtos.RegisterRequest request) {
        if (userRepository.existsByEmail(request.email)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already registered"));
        }
        User user = new User();
        user.setEmail(request.email);
        user.setPasswordHash(passwordEncoder.encode(request.password));
        user.setRoles(Set.of(Role.USER));
        userRepository.save(user);
        String token = jwtService.generateToken(user.getEmail(), Map.of("roles", user.getRoles().stream().map(Enum::name).toList()));
        return ResponseEntity.ok(new AuthDtos.AuthResponse(token));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDtos.LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email, request.password));
        String token = jwtService.generateToken(request.email, Map.of("roles", auth.getAuthorities().stream().map(a -> a.getAuthority().replace("ROLE_", "")).toList()));
        return ResponseEntity.ok(new AuthDtos.AuthResponse(token));
    }
}


