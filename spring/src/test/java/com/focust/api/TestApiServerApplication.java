/**
 * TestServerApplication.java - Runs Tests on Spring Application
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
 * @version 0.0.1
 * @since 0.0.1
 */
package com.focust.api;

///////////////////////////////////////////////////////////////////////////

// Spring Framework //
import org.springframework.boot.SpringApplication;

///////////////////////////////////////////////////////////////////////////

public class TestApiServerApplication {

	public static void main(String[] args) {
		SpringApplication.from(ApiServerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
