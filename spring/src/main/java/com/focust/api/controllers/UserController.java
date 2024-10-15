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
 * This controller does not handle receiving requests to create a user or add
 * a new one to the database, as that is separately handled by the
 * AuthenticationController.
 * @see com.focust.api.controllers.AuthenticationController
 *
 * @author Allan DeBoe (allan.m.deboe@gmail.com)
 * @version 0.0.3
 * @since 0.0.3
 */
package com.focust.api.controllers;

///////////////////////////////////////////////////////////////////////////

// Focust //
import com.focust.api.dto.requests.PageNumberRequest;
import com.focust.api.dto.responses.NonSensitiveUserDataResponse;
import com.focust.api.exceptions.EmptyPageException;
import com.focust.api.exceptions.UserNotFoundException;
import com.focust.api.users.UserService;

// Spring Framework //
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Standard Java //
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

///////////////////////////////////////////////////////////////////////////

@RestController
@RequestMapping(value="/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value="", produces="application/json")
    public ResponseEntity<Object> getUsers(PageNumberRequest request) {

        int pageNumber = Optional.ofNullable(request).map(PageNumberRequest::getPageNumber).orElse(1);
        if (pageNumber < 1) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "the page number must be greater than or equal to 1!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            Pageable page = PageRequest.of(pageNumber-1, 15);
            return new ResponseEntity<>(userService.getUsers(page), HttpStatus.OK);
        }
        catch (EmptyPageException e) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

    }

    @GetMapping(value="/{id}", produces="application/json")
    public ResponseEntity<Object> getUserById(@PathVariable long id) {
        try {
            NonSensitiveUserDataResponse response = userService.getNonSensitiveUserDetails(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (UserNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }
    }

}
