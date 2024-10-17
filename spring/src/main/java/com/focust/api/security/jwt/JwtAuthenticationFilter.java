/**
 * JwtAuthenticationFilter.java - Used to check if a request has a valid JWT token.
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
 * @author Allan DeBoe (allan.m.deboe@gmail.com)
 * @version 0.0.4
 * @since 0.0.3
 */

package com.focust.api.security.jwt;

///////////////////////////////////////////////////////////////////////////

// Focust //
import com.focust.api.exceptions.UserNotFoundException;
import com.focust.api.users.UserJwtDetails;
import com.focust.api.users.UserService;

// Jakarta Servlets //
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Project Lombok //
import lombok.RequiredArgsConstructor;

// Spring Framework //
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

// Standard Java //
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

///////////////////////////////////////////////////////////////////////////

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired private JwtService jwtService;
    @Autowired private UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        final Optional<String> authorizationHeader = Optional.ofNullable(request.getHeader("Authorization"));
        if (authorizationHeader.filter(h -> h.startsWith("Bearer ")).isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        // "7" is the character length of "Bearer "
        Optional<String> jwtToken = authorizationHeader.map(h -> h.substring(7));

        if (jwtToken.filter(bearer -> !bearer.isBlank()).isEmpty()) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Received JWT Token with Empty Bearer Header"
            );
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String email = jwtService.getEmail(jwtToken.get()).orElseThrow(Exception::new);
            UserJwtDetails jwtDetails = userService.getUserDetails(email);
        }

        // "NoSuchAlgorithmException" and "InvalidKeySpecException" are thrown as a
        // result of using the wrong Algorithm or Key Specification when JWTService
        // extracts the Private and Public Keys needed to sign and verify JWT Tokens.
        // Since the code should be correct, these exceptions will never actually be thrown,
        // but, just in case of a stupid coding mistake in the future, I have this catch
        // statement here to ensure that the server can still run while also making sure
        // that someone cannot get unauthorized access to user data or functionality.
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Something went horribly wrong when validating received JWT Token"
            );
        }
        catch (UserNotFoundException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Unable to find existing user with the email from received JWT Token"
            );
        }
        catch (Exception e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Received JWT Token is Invalid"
            );
        }

        filterChain.doFilter(request, response);
    }

}
