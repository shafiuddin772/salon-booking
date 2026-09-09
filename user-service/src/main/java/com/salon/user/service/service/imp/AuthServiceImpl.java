 package com.salon.user.service.service.imp;

import com.salon.user.service.model.User;
import com.salon.user.service.payload.dto.SignupDTO;
import com.salon.user.service.payload.response.AuthResponse;
import com.salon.user.service.payload.response.TokenResponse;
import com.salon.user.service.repository.UserRepository;
import com.salon.user.service.service.AuthService;
import com.salon.user.service.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final KeycloakService keycloakService;


    // =========================================================
    // LOGIN
    // =========================================================

    @Override
    public AuthResponse login(
            String username,
            String password) {

        /*
         * Get token from Keycloak
         */
        TokenResponse tokenResponse =
                keycloakService.getAdminAccessToken(
                        username,
                        password,
                        "password",
                        null
                );


        /*
         * Find user from our database
         */
        User user =
                userRepository.findByUsername(username);


        if (user == null) {
            throw new RuntimeException(
                    "User not found"
            );
        }


        /*
         * Create response
         */
        AuthResponse authResponse =
                new AuthResponse();

        authResponse.setAccessToken(
                tokenResponse.getAccessToken()
        );

        authResponse.setRefreshToken(
                tokenResponse.getRefreshToken()
        );

        authResponse.setRole(
                user.getRole()
        );

        authResponse.setMessage(
                "Login successful"
        );


        return authResponse;
    }


    // =========================================================
    // SIGNUP
    // =========================================================

    @Override
    public AuthResponse signup(
            SignupDTO req) throws Exception {

        /*
         * Step 1:
         * Create user in Keycloak
         */
        keycloakService.createUser(req);


        /*
         * Step 2:
         * Create user in our database
         */
        User user =
                new User();

        user.setUsername(
                req.getUsername()
        );

        /*
         * IMPORTANT:
         * Ideally don't store the plain password
         * in your own database when Keycloak
         * is responsible for authentication.
         */
        user.setPassword(
                req.getPassword()
        );

        user.setEmail(
                req.getEmail()
        );

        user.setRole(
                req.getRole().toString()
        );

        user.setFullName(
                req.getFirstName()
        );

        user.setCreatedAt(
                LocalDateTime.now()
        );


        /*
         * Save user
         */
        userRepository.save(user);


        /*
         * Step 3:
         * Login user automatically after signup
         */
        TokenResponse tokenResponse =
                keycloakService.getAdminAccessToken(
                        req.getUsername(),
                        req.getPassword(),
                        "password",
                        null
                );


        /*
         * Step 4:
         * Create response
         */
        AuthResponse authResponse =
                new AuthResponse();

        authResponse.setAccessToken(
                tokenResponse.getAccessToken()
        );

        authResponse.setRefreshToken(
                tokenResponse.getRefreshToken()
        );

        authResponse.setRole(
                user.getRole()
        );

        authResponse.setMessage(
                "Registration successful"
        );


        return authResponse;
    }


    // =========================================================
    // REFRESH TOKEN
    // =========================================================

    @Override
    public AuthResponse getAccessTokenFromRefreshToken(
            String refreshToken) {

        /*
         * Ask Keycloak for a new access token
         */
        TokenResponse tokenResponse =
                keycloakService.getAdminAccessToken(
                        null,
                        null,
                        "refresh_token",
                        refreshToken
                );


        /*
         * Create response
         */
        AuthResponse authResponse =
                new AuthResponse();

        authResponse.setAccessToken(
                tokenResponse.getAccessToken()
        );

        authResponse.setRefreshToken(
                tokenResponse.getRefreshToken()
        );

        authResponse.setMessage(
                "Access token refreshed successfully"
        );


        return authResponse;
    }
}

