/**
 * CorsConfigurationTests - Tests regarding the back-end server's CORS configuration.
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
 * This class is used to ensure that the CORS configuration of the
 * Spring application meets expected behavior.
 *
 * @see com.focust.api.security.SecurityConfiguration
 *
 * @author Allan DeBoe (allan.m.deboe@gmail.com)
 * @version 0.0.2
 * @since 0.0.2
 */
package com.focust.api;

///////////////////////////////////////////////////////////////////////////

// JUnit / Jupiter //
import org.junit.jupiter.api.Test;

// Spring Framework //
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.web.servlet.MockMvc;

// Standard Java //
import java.util.Arrays;
import java.util.List;

// Static Imports //
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

///////////////////////////////////////////////////////////////////////////

@SpringBootTest
@AutoConfigureMockMvc
class CorsConfigurationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Environment environment;

    @Test
    public final void testAllowedOriginsAndMethods() throws Exception {
        final List<String> expectedOrigins = Arrays.asList(
                "https://localhost:" + environment.getProperty("server.port")
        );
        final String expectedMethods = "GET";

        for (String origin : expectedOrigins) {
            mockMvc.perform(options("/**")
                    .header("Origin", origin)
                    .header("Access-Control-Request-Method", "GET")
            ).andExpect(status().isOk())
                    .andExpect(header().string("Access-Control-Allow-Origin", origin))
                    .andExpect(header().string("Access-Control-Allow-Methods", expectedMethods));
        }
    }

}
