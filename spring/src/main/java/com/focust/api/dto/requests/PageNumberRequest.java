/**
 * PageNumberRequest.java - Request for the number of pages of entries of a table
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
package com.focust.api.dto.requests;

///////////////////////////////////////////////////////////////////////////

// Project Lombok //
import lombok.Getter;
import lombok.Setter;

///////////////////////////////////////////////////////////////////////////

@Setter
@Getter
public final class PageNumberRequest implements Request {

    private int pageNumber = 1;

    @Override
    public String getJson() {
        return "{ \"pageNumber\": " + pageNumber + " }";
    }

}
