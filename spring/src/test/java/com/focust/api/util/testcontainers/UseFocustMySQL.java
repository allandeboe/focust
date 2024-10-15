/**
 * UseFocustMySQL.java - Annotation for MySQL Testcontainer
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
 * This Annotation is primarily used to indicate that the test should use
 * the MySQL Testcontainer for testing.
 * @see com.focust.api.util.testcontainers.FocustMySQLExtension
 * @see com.focust.api.util.testcontainers.FocustMySQLContainer
 *
 * @author Allan DeBoe (allan.m.deboe@gmail.com)
 * @version 0.0.3
 * @since 0.0.3
 */
package com.focust.api.util.testcontainers;

///////////////////////////////////////////////////////////////////////////

// JUnit 5 (Jupiter) //
import org.junit.jupiter.api.extension.ExtendWith;

// Standard Java //
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

///////////////////////////////////////////////////////////////////////////

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(FocustMySQLExtension.class)
public @interface UseFocustMySQL { }
