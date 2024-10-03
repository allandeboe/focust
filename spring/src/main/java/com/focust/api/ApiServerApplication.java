/**
 * ApiServerApplication.java - Entrypoint of the Spring Application
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

///////////////////////////////////////////////////////////////////////////

@SpringBootApplication
public class ApiServerApplication {

	@Autowired
	Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(ApiServerApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfiguration() {
		return new WebMvcConfigurer() {
			public void addCorsMapping(CorsRegistry registry) {
				String url = "http://localhost:" + environment.getProperty("server.port");
				registry.addMapping("/**").allowedOrigins(url);
			}
		};
	}

}
