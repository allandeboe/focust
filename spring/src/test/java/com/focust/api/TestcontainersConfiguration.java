/**
 * TestcontainersConfiguration.java - Configuration for MySQL Testcontainer
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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;

// Testcontainers //
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

///////////////////////////////////////////////////////////////////////////

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	@Bean
	@ServiceConnection
	public MySQLContainer<?> mysqlContainer() {
		return new MySQLContainer<>(DockerImageName.parse("mysql:latest")).withDatabaseName("focust_db");
	}

}
