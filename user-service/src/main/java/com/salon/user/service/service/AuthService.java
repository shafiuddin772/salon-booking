
        package com.salon.user.service.service;

import com.salon.user.service.payload.dto.SignupDTO;
import com.salon.user.service.payload.response.AuthResponse;

public interface AuthService {

    AuthResponse login(String username, String password);

    AuthResponse signup(SignupDTO req) throws Exception;

    AuthResponse getAccessTokenFromRefreshToken(
            String refreshToken
    );
}

