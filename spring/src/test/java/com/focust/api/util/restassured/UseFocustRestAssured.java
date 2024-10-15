/**
 * UseFocustRestAssured.java - Annotation for REST-Assured Extension
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
 * This Annotation is primarily used to indicate that REST-Assured is used.
 * @see com.focust.api.util.restassured.FocustRestAssuredExtension
 *
 * For this annotation to work properly, the test class must have the data
 * members with the following types & annotations:
 *
 * - @Autowired Environment
 * - @Autowired ResourceLoader
 * - @LocalServerPort int
 *
 * For an example, here are a list of classes that have these:
 * @see com.focust.api.controllers.UserIntegrationTests
 *
 * @author Allan DeBoe (allan.m.deboe@gmail.com)
 * @version 0.0.3
 * @since 0.0.3
 */
package com.focust.api.util.restassured;

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
@ExtendWith(FocustRestAssuredExtension.class)
public @interface UseFocustRestAssured { }