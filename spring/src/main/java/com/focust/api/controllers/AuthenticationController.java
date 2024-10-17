/**
 * AuthenticationController.java - REST Controller used to manage Authentication
 * Copyright (C) 2024  Allan DeBoe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * ------------------------------------------------------------------------
 *
 * This controller is used to handle user creation (registering an account)
 * as well as signing a user in.
 *
 * @see com.focust.api.users.User
 *
 * @author Allan DeBoe (allan.m.deboe@gmail.com)
 * @version 0.0.4
 * @since 0.0.3
 */
package com.focust.api.controllers;

///////////////////////////////////////////////////////////////////////////

// Focust //
import com.focust.api.exceptions.IncorrectSignInException;
import com.focust.api.exceptions.UserAlreadyExistsException;
import com.focust.api.exceptions.UserNotFoundException;
import com.focust.api.dto.responses.JwtTokenResponse;
import com.focust.api.security.jwt.JwtService;
import com.focust.api.dto.requests.RegisterUserRequest;
import com.focust.api.dto.requests.SignInUserRequest;
import com.focust.api.users.UserJwtDetails;
import com.focust.api.users.UserService;

// Spring Framework //
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Standard Java //
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

///////////////////////////////////////////////////////////////////////////

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    /**
     * @param request a RegisterUserRequest representing the JSON Request
     * @return an HTTP Response, with JWT Access Token generated if a new user is created.
     */
    @PostMapping(value="/register", produces="application/json")
    public final ResponseEntity<Object> registerUser(@RequestBody RegisterUserRequest request, HttpServletResponse servletResponse) {
        try {
            UserJwtDetails userDetails = userService.createUser(request);
            Optional<String> accessToken = jwtService.generateAccessToken(userDetails);
            Optional<String> refreshToken = jwtService.generateRefreshToken(userDetails);
            if (accessToken.isEmpty() || refreshToken.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "The generated JWT Token is empty!");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            Cookie refreshTokenCookie = new Cookie ("jwt-refresh-token", refreshToken.get());
            refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
            refreshTokenCookie.setSecure(true);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setPath("/");
            servletResponse.addCookie(refreshTokenCookie);

            return new ResponseEntity<>(new JwtTokenResponse(accessToken.get(), userDetails.getId()), HttpStatus.CREATED);
        }

        // To ensure someone cannot just get the users authentication token
        // by trying to register an existing user, the body does not return
        // JWT Tokens
        catch (UserAlreadyExistsException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        // "NoSuchAlgorithmException" and "InvalidKeySpecException" are thrown as a
        // result of using the wrong Algorithm or Key Specification when JWTService
        // extracts the Private and Public Keys needed to sign and verify JWT Tokens.
        // Since the code should be correct, these exceptions will never actually be thrown,
        // but, just in case of a stupid coding mistake in the future, I have this catch
        // statement here to ensure that the server can still run while also making sure
        // that someone cannot get unauthorized access to user data or functionality.
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Something went horribly wrong when registering the user!");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * @param request a SignInUserRequest representing the JSON Request
     * @return an HTTP Response, with JWT Access Token generated if the login has been successful.
     */
    @PostMapping(value="/login", produces="application/json")
    public final ResponseEntity<Object> signInUser(@RequestBody SignInUserRequest request, HttpServletResponse servletResponse) {
        try {
            UserJwtDetails userDetails = userService.verifyUserSignIn(request);
            Optional<String> accessToken = jwtService.generateAccessToken(userDetails);
            Optional<String> refreshToken = jwtService.generateRefreshToken(userDetails);
            if (accessToken.isEmpty() || refreshToken.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "The generated JWT Token is empty!");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            Cookie refreshTokenCookie = new Cookie ("jwt-refresh-token", refreshToken.get());
            refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
            refreshTokenCookie.setSecure(true);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setPath("/");
            servletResponse.addCookie(refreshTokenCookie);

            return new ResponseEntity<>(new JwtTokenResponse(accessToken.get(), userDetails.getId()), HttpStatus.OK);
        }

        // To ensure someone cannot just get the users authentication token
        // by trying to either login as a user that doesn't exist or done a failed login
        // attempt, the body does not return JWT Tokens
        catch (UserNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }
        catch (IncorrectSignInException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "The provided username (email) or password is incorrect. Please try again!");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        // "NoSuchAlgorithmException" and "InvalidKeySpecException" are thrown as a
        // result of using the wrong Algorithm or Key Specification when JWTService
        // extracts the Private and Public Keys needed to sign and verify JWT Tokens.
        // Since the code should be correct, these exceptions will never actually be thrown,
        // but, just in case of a stupid coding mistake in the future, I have this catch
        // statement here to ensure that the server can still run while also making sure
        // that someone cannot get unauthorized access to user data or functionality.
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Something went horribly wrong when registering the user!");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
