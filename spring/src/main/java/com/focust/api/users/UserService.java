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
 * @version 0.0.4
 * @since 0.0.3
 */
package com.focust.api.users;

///////////////////////////////////////////////////////////////////////////

// Focust //
import com.focust.api.controllers.AuthenticationController;
import com.focust.api.dto.requests.RegisterUserRequest;
import com.focust.api.dto.requests.SignInUserRequest;
import com.focust.api.dto.responses.NonSensitiveUserDataResponse;
import com.focust.api.exceptions.EmptyPageException;
import com.focust.api.exceptions.IncorrectSignInException;
import com.focust.api.exceptions.UserAlreadyExistsException;
import com.focust.api.exceptions.UserNotFoundException;
import com.focust.api.security.bcrypt.BCryptHash;

// Spring Framework //
import com.focust.api.security.jwt.JwtAuthenticationFilter;
import com.focust.api.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Standard Java //
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

///////////////////////////////////////////////////////////////////////////

@Service
public class UserService {

    @Autowired private UserRepository userRepository;

    // UserService is the only place where password encoding and matching are even needed.
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2B, 12);

    /**
     * This function is primarily used when verifying a JWT Token
     * associated with the user. Emails are essentially usernames
     * in the context of the application, hence why this exists.
     *
     * @see JwtService
     * @see JwtAuthenticationFilter
     *
     * @param email the email of the user
     * @return a UserJWTDetails object based on the user with the email
     * @throws UserNotFoundException if the user with the email is not found
     */
    public final UserJwtDetails getUserDetails(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return new UserJwtDetails(user);
    }

    /**
     * This function is used when verifying a user log in, to which,
     * if the login is successful, a JWT Token gets returned.
     *
     * @see JwtService
     * @see AuthenticationController
     *
     * @param request a SignInUserRequest representing the JSON request
     * @return a UserJWTDetails object based on the user with the email
     * @throws UserNotFoundException if the user with the email is not found
     */
    public final UserJwtDetails verifyUserSignIn(SignInUserRequest request) throws UserNotFoundException, IncorrectSignInException {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash().toString())) {
            throw new IncorrectSignInException();
        }

        return new UserJwtDetails(user);
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
    public final UserJwtDetails createUser(RegisterUserRequest request) throws UserAlreadyExistsException {

        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) throw new UserAlreadyExistsException();

        BCryptHash hash = new BCryptHash(passwordEncoder.encode(request.getPassword()));

        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setPasswordHash(hash);
        userRepository.save(newUser);

        return new UserJwtDetails(newUser);
    }

    /**
     * Used to get non-sensitive data regarding the user.
     *
     * @param id the id of the user
     * @return a NonSensitiveUserDataResponse representing the JSON response.
     * @throws UserNotFoundException if the user was unable to be found.
     */
    public final NonSensitiveUserDataResponse getNonSensitiveUserDetails(long id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return new NonSensitiveUserDataResponse(user.getId(), user.getEmail());
    }

    /**
     * @param page Pageable representing the page
     * @return a list of user data encoded in NonSensitiveUserDataResponse objects
     * @throws EmptyPageException if there are
     */
    public final List<NonSensitiveUserDataResponse> getUsers(Pageable page) throws EmptyPageException {
        Page<User> pageEntries = userRepository.findAll(page);
        if (pageEntries.isEmpty()) {
            throw new EmptyPageException();
        }
        List<NonSensitiveUserDataResponse> list = new ArrayList<>();
        for (User entry: pageEntries) {
            NonSensitiveUserDataResponse response = new NonSensitiveUserDataResponse(entry.getId(), entry.getEmail());
            list.add(response);
        }
        return list;
    }

}
