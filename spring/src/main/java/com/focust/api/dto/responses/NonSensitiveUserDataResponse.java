/**
 * NonSensitiveUserDataResponse.java - Response containing non-sensitive user data.
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
 * @see com.focust.api.users.User
 * @see com.focust.api.controllers.UserController
 *
 * @author Allan DeBoe (allan.m.deboe@gmail.com)
 * @version 0.0.3
 * @since 0.0.3
 */
package com.focust.api.dto.responses;

///////////////////////////////////////////////////////////////////////////

// Project Lombok //
import lombok.Getter;
import lombok.RequiredArgsConstructor;

///////////////////////////////////////////////////////////////////////////

@Getter
@RequiredArgsConstructor
public final class NonSensitiveUserDataResponse {

    private final Long id;
    private final String email;

}
