/**
 * EmptyPageException.java - Thrown if received an empty page.
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
 * @see org.springframework.data.domain.Page
 *
 * @author Allan DeBoe (allan.m.deboe@gmail.com)
 * @version 0.0.4
 * @since 0.0.3
 */
package com.focust.api.exceptions;

///////////////////////////////////////////////////////////////////////////

public final class EmptyPageException extends RuntimeException { }
