/**
 * JwtAccessTokenResponse.java - DTO Response containing just a JWT Access Token
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
 * This class corresponds to the JSON response after registering a user,
 * which merely contains a JWT Token that the client is meant to store
 * for later.
 *
 * @see com.focust.api.security.jwt.JwtService
 * @see com.focust.api.controllers.AuthenticationController
 *
 * @author Allan DeBoe (allan.m.deboe@gmail.com)
 * @version 0.0.5
 * @since 0.0.3
 */
package com.focust.api.dto.responses;

///////////////////////////////////////////////////////////////////////////

// Project Lombok //
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

///////////////////////////////////////////////////////////////////////////

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public final class JwtTokenResponse {

    private final String accessToken;
    private long userId;

}
