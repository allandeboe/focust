/**
 * UserService.java - Service to handle the "users" table.
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
 * Since we ideally don't want direct external access to the "Users"
 * class, this service exists.
 *
 * @see com.focust.api.users.User
 *
 * @author Allan DeBoe (allan.m.deboe@gmail.com)
 * @version 0.0.3
 * @since 0.0.3
 */
package com.focust.api.users;

///////////////////////////////////////////////////////////////////////////

// Focust //
import com.focust.api.controllers.AuthenticationController;
import com.focust.api.dto.requests.RegisterUserRequest;
import com.focust.api.dto.requests.SignInUserRequest;
import com.focust.api.exceptions.IncorrectSignInException;
import com.focust.api.exceptions.UserAlreadyExistsException;
import com.focust.api.exceptions.UserNotFoundException;
import com.focust.api.security.bcrypt.BCryptHash;

// Spring Framework //
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Standard Java //
import java.util.Optional;

///////////////////////////////////////////////////////////////////////////

@Service
public class UserService {

    @Autowired private UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2B, 12);
    }

    /**
     * This function is primarily used when verifying a JWT Token
     * associated with the user. Emails are essentially usernames
     * in the context of the application, hence why this exists.
     *
     * @see com.focust.api.security.jwt.JWTService
     * @see com.focust.api.security.jwt.JWTAuthenticationFilter
     *
     * @param email the email of the user
     * @return a UserJWTDetails object based on the user with the email
     * @throws UserNotFoundException if the user with the email is not found
     */
    public final UserJWTDetails getUserDetails(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return new UserJWTDetails(user);
    }

    /**
     * This function is used when verifying a user log in, to which,
     * if the login is successful, a JWT Token gets returned.
     *
     * @see com.focust.api.security.jwt.JWTService
     * @see AuthenticationController
     *
     * @param request a SignInUserRequest representing the JSON request
     * @return a UserJWTDetails object based on the user with the email
     * @throws UserNotFoundException if the user with the email is not found
     */
    public final UserJWTDetails verifyUserSignIn(SignInUserRequest request) throws UserNotFoundException, IncorrectSignInException {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder().matches(request.getPassword(), user.getPasswordHash().toString())) {
            throw new IncorrectSignInException();
        }

        return new UserJWTDetails(user);
    }

    /**
     * Creating a new user, outside of testing, is only done when registering
     * a new account. Since a JWT token needs to be sent back to the client,
     * which requires the user's email, this function returns a UserJWTDetails
     * object based on the newly created user.
     *
     * @param request a RegisterUserRequest representing the JSON request
     * @return a UserJWTDetails object used to generate an access token
     */
    public final UserJWTDetails createUser(RegisterUserRequest request) throws UserAlreadyExistsException {

        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) throw new UserAlreadyExistsException();

        BCryptHash hash = new BCryptHash(passwordEncoder().encode(request.getPassword()));

        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setPasswordHash(hash);
        userRepository.save(newUser);

        return new UserJWTDetails(newUser);
    }

    

}
