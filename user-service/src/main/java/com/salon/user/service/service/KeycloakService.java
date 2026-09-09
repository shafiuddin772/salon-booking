
        package com.salon.user.service.service;

import com.salon.user.service.payload.dto.*;
import com.salon.user.service.payload.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private static final String KEYCLOAK_BASE_URL =
            "http://localhost:8080";

    /*
     * Realm
     */
    private static final String REALM = "master";

    /*
     * Admin API
     */
    private static final String KEYCLOAK_ADMIN_API =
            KEYCLOAK_BASE_URL
                    + "/admin/realms/"
                    + REALM
                    + "/users";

    /*
     * Token API
     */
    private static final String TOKEN_URL =
            KEYCLOAK_BASE_URL
                    + "/realms/"
                    + REALM
                    + "/protocol/openid-connect/token";

    /*
     * Your Keycloak client
     */
    private static final String CLIENT_ID =
            "salon-booking-client";

    private static final String CLIENT_SECRET =
            "kJTnmk51RhUh3HeySLfQBuD1qqP8pSnaRhJUcD7BShJvIvPIpynQa3gY8ansg42zuIBd8kIB2TGs8VVkA1MMXS";

    /*
     * Admin username/password
     */
    private static final String ADMIN_USERNAME = "zosh";
    private static final String ADMIN_PASSWORD = "admin";

    private static final String GRANT_TYPE = "password";

    private static final String SCOPE =
            "openid profile email";

    /*
     * IMPORTANT:
     * This should be the CLIENT UUID from Keycloak,
     * NOT the client-id/name.
     */
    private static final String CLIENT_UUID =
            "b82232b3-0c67-4c0c-bdf1-dd0372e81e6c";

    private final RestTemplate restTemplate;


    // =========================================================
    // CREATE USER
    // =========================================================

    public void createUser(SignupDTO signupDTO) throws Exception {

        /*
         * Step 1:
         * Get admin access token
         */
        String accessToken =
                getAdminAccessToken(
                        ADMIN_USERNAME,
                        ADMIN_PASSWORD,
                        GRANT_TYPE,
                        null
                ).getAccessToken();


        /*
         * Step 2:
         * Create password credential
         */
        Credential credential = new Credential();

        credential.setTemporary(false);
        credential.setType("password");
        credential.setValue(signupDTO.getPassword());


        /*
         * Step 3:
         * Create Keycloak user request
         */
        UserRequest userRequest = new UserRequest();

        userRequest.setUsername(signupDTO.getUsername());
        userRequest.setEnabled(true);
        userRequest.setFirstName(signupDTO.getFirstName());
        userRequest.setLastName(signupDTO.getLastName());
        userRequest.setEmail(signupDTO.getEmail());

        /*
         * IMPORTANT:
         * Add password to user request.
         */
        List<Credential> credentials = new ArrayList<>();
        credentials.add(credential);

        userRequest.setCredentials(credentials);


        /*
         * Step 4:
         * Headers
         */
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);


        /*
         * Step 5:
         * Request entity
         */
        HttpEntity<UserRequest> requestEntity =
                new HttpEntity<>(userRequest, headers);


        /*
         * Step 6:
         * Create user
         */
        ResponseEntity<String> response =
                restTemplate.exchange(
                        KEYCLOAK_ADMIN_API,
                        HttpMethod.POST,
                        requestEntity,
                        String.class
                );


        /*
         * Step 7:
         * Check response
         */
        if (response.getStatusCode() == HttpStatus.CREATED) {

            System.out.println("User created successfully");


            /*
             * Step 8:
             * Find created user
             */
            KeycloakUserDTO user =
                    fetchFirstUserByEmail(
                            signupDTO.getUsername(),
                            accessToken
                    );


            if (user == null) {
                throw new RuntimeException(
                        "User was created but could not be found"
                );
            }


            /*
             * Step 9:
             * Find requested role
             */
            KeycloakRole role =
                    getRoleByName(
                            CLIENT_UUID,
                            accessToken,
                            signupDTO.getRole().name()
                    );


            if (role == null) {
                throw new RuntimeException(
                        "Role not found: "
                                + signupDTO.getRole().name()
                );
            }


            /*
             * Step 10:
             * Put role inside List
             */
            List<KeycloakRole> roles =
                    new ArrayList<>();

            roles.add(role);


            /*
             * Step 11:
             * Assign role to user
             */
            assignRoleToUser(
                    user.getId(),
                    CLIENT_UUID,
                    roles,
                    accessToken
            );

            System.out.println(
                    "Role assigned successfully"
            );

        } else {

            throw new RuntimeException(
                    "Failed to create Keycloak user. Status: "
                            + response.getStatusCode()
            );
        }
    }


    // =========================================================
    // GET ADMIN ACCESS TOKEN
    // =========================================================

    public TokenResponse getAdminAccessToken(
            String username,
            String password,
            String grantType,
            String refreshToken) {

        /*
         * Keycloak token endpoint expects
         * application/x-www-form-urlencoded
         */
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(
                MediaType.APPLICATION_FORM_URLENCODED
        );


        /*
         * Form data
         */
        org.springframework.util.MultiValueMap<String, String>
                formData =
                new org.springframework.util.LinkedMultiValueMap<>();


        formData.add(
                "client_id",
                CLIENT_ID
        );

        formData.add(
                "client_secret",
                CLIENT_SECRET
        );

        formData.add(
                "grant_type",
                grantType
        );


        /*
         * Password grant
         */
        if ("password".equals(grantType)) {

            formData.add(
                    "username",
                    username
            );

            formData.add(
                    "password",
                    password
            );

            formData.add(
                    "scope",
                    SCOPE
            );
        }


        /*
         * Refresh token grant
         */
        if ("refresh_token".equals(grantType)
                && refreshToken != null) {

            formData.add(
                    "refresh_token",
                    refreshToken
            );
        }


        /*
         * Request
         */
        HttpEntity<
                org.springframework.util.MultiValueMap<String, String>
                > requestEntity =
                new HttpEntity<>(
                        formData,
                        headers
                );


        /*
         * Call Keycloak
         */
        ResponseEntity<TokenResponse> response =
                restTemplate.exchange(
                        TOKEN_URL,
                        HttpMethod.POST,
                        requestEntity,
                        TokenResponse.class
                );


        if (!response.getStatusCode().is2xxSuccessful()
                || response.getBody() == null) {

            throw new RuntimeException(
                    "Failed to get Keycloak access token"
            );
        }


        return response.getBody();
    }


    // =========================================================
    // GET ROLE BY NAME
    // =========================================================

    public KeycloakRole getRoleByName(
            String clientUuid,
            String token,
            String role) {

        /*
         * Client roles API:
         *
         * GET
         * /admin/realms/{realm}/clients/{client-uuid}/roles
         */
        String url =
                KEYCLOAK_BASE_URL
                        + "/admin/realms/"
                        + REALM
                        + "/clients/"
                        + clientUuid
                        + "/roles";


        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(token);
        headers.setAccept(
                List.of(MediaType.APPLICATION_JSON)
        );


        HttpEntity<Void> requestEntity =
                new HttpEntity<>(headers);


        /*
         * Call Keycloak
         */
        ResponseEntity<KeycloakRole[]> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        requestEntity,
                        KeycloakRole[].class
                );


        if (!response.getStatusCode().is2xxSuccessful()
                || response.getBody() == null) {

            throw new RuntimeException(
                    "Failed to fetch Keycloak roles"
            );
        }


        /*
         * Find requested role
         */
        for (KeycloakRole keycloakRole :
                response.getBody()) {

            if (keycloakRole.getName() != null
                    && keycloakRole.getName()
                    .equalsIgnoreCase(role)) {

                return keycloakRole;
            }
        }


        return null;
    }


    // =========================================================
    // FIND USER BY USERNAME
    // =========================================================

    public KeycloakUserDTO fetchFirstUserByEmail(
            String username,
            String token) {

        /*
         * Keycloak users API supports
         *
         * GET /admin/realms/{realm}/users
         *
         * ?username=...
         */
        String url =
                UriComponentsBuilder
                        .fromUriString(KEYCLOAK_ADMIN_API)
                        .queryParam(
                                "username",
                                username
                        )
                        .build()
                        .toUriString();


        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(token);
        headers.setAccept(
                List.of(MediaType.APPLICATION_JSON)
        );


        HttpEntity<Void> requestEntity =
                new HttpEntity<>(headers);


        /*
         * Call Keycloak
         */
        ResponseEntity<KeycloakUserDTO[]> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        requestEntity,
                        KeycloakUserDTO[].class
                );


        if (!response.getStatusCode().is2xxSuccessful()
                || response.getBody() == null) {

            throw new RuntimeException(
                    "Failed to fetch Keycloak user"
            );
        }


        /*
         * Return first matching user
         */
        if (response.getBody().length > 0) {

            return response.getBody()[0];
        }


        return null;
    }


    // =========================================================
    // ASSIGN CLIENT ROLE TO USER
    // =========================================================

    public void assignRoleToUser(
            String userId,
            String clientUuid,
            List<KeycloakRole> roles,
            String token) {

        /*
         * Keycloak endpoint:
         *
         * POST
         * /admin/realms/{realm}/users/{user-id}
         * /role-mappings/clients/{client-id}
         */
        String url =
                KEYCLOAK_BASE_URL
                        + "/admin/realms/"
                        + REALM
                        + "/users/"
                        + userId
                        + "/role-mappings/clients/"
                        + clientUuid;


        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(token);

        headers.setContentType(
                MediaType.APPLICATION_JSON
        );


        /*
         * Request body is a JSON array
         * of role representations.
         */
        HttpEntity<List<KeycloakRole>> requestEntity =
                new HttpEntity<>(
                        roles,
                        headers
                );


        /*
         * Assign role
         */
        ResponseEntity<Void> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        requestEntity,
                        Void.class
                );


        /*
         * Keycloak returns 204 when successful.
         */
        if (response.getStatusCode()
                != HttpStatus.NO_CONTENT) {

            throw new RuntimeException(
                    "Failed to assign role. Status: "
                            + response.getStatusCode()
            );
        }
    }
}

