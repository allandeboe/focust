/**
 * FocustMySQLContainer.java - MySQL Testcontainer for Focust
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
 * This class represents the MySQL Testcontainer used when running
 * tests that may require the use of the database (like integration tests).
 *
 * @see com.focust.api.util.testcontainers.FocustMySQLExtension
 * @see com.focust.api.util.testcontainers.UseFocustMySQL
 *
 * @author Allan DeBoe (allan.m.deboe@gmail.com)
 * @version 0.0.3
 * @since 0.0.3
 */
package com.focust.api.util.testcontainers;

///////////////////////////////////////////////////////////////////////////

// Testcontainers //
import org.testcontainers.containers.MySQLContainer;

///////////////////////////////////////////////////////////////////////////

public final class FocustMySQLContainer extends MySQLContainer<FocustMySQLContainer> {

    private static final String IMAGE_VERSION = "mysql:latest";
    private static FocustMySQLContainer databaseContainer;

    private FocustMySQLContainer() {
        super(IMAGE_VERSION);
    }

    public static FocustMySQLContainer getInstance() {
        if (databaseContainer == null) {
            databaseContainer = new FocustMySQLContainer()
                    .withDatabaseName("focust_db")
                    .withNetworkMode("spring-mysql")
                    .withExposedPorts(3306);
        }
        return databaseContainer;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("spring.datasource.url", databaseContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", databaseContainer.getUsername());
        System.setProperty("spring.datasource.password", databaseContainer.getPassword());
    }

    @Override
    public void stop() {
        // Testcontainers will automatically handle shut down of this container.
    }
}
