/**
 * SecurityConfiguration.java - Configuration for Security of Spring Application.
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
 * @version 0.0.2
 * @since 0.0.2
 */
package com.focust.api.security;

///////////////////////////////////////////////////////////////////////////

// Spring Framework //
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// Standard Java //
import java.util.Arrays;

///////////////////////////////////////////////////////////////////////////

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors_config = new CorsConfiguration();
        cors_config.setAllowedOrigins(Arrays.asList("https://localhost:" + environment.getProperty("server.port")));
        cors_config.setAllowedMethods(Arrays.asList("GET"));

        UrlBasedCorsConfigurationSource config_source = new UrlBasedCorsConfigurationSource();
        config_source.registerCorsConfiguration("/**", cors_config);
        return config_source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http_security) throws Exception {

        http_security.requiresChannel(channel -> channel.anyRequest().requiresSecure());
        http_security.authorizeHttpRequests(request -> request.anyRequest().permitAll());

        return http_security.build();
    }

}
