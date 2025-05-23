/**
 * BCryptHashingUnitTests.java - Tests regarding using the "BCryptHash" class
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
 * This class is used to ensure that the BCryptHash works
 * as expected.
 *
 * @see com.focust.api.security.bcrypt.BCryptHash
 *
 * @author Allan DeBoe (allan.m.deboe@gmail.com)
 * @version 0.0.3
 * @since 0.0.3
 */
package com.focust.api.unit.security;

///////////////////////////////////////////////////////////////////////////

// Focust //
import com.focust.api.security.bcrypt.BCryptHash;

// JUnit 5 (Jupiter) //
import org.junit.jupiter.api.Test;

// Spring Framework //
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Static Imports //
import static org.assertj.core.api.Assertions.assertThat;

///////////////////////////////////////////////////////////////////////////

public class BCryptHashingUnitTests {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2B, 12);

    @Test
    public final void givenBCryptHash_whenGettingHashString_hashStringsAreEqual() {

        String testPassword = "abc123xyz";
        final String testHashString = passwordEncoder.encode(testPassword);

        BCryptHash testHash = new BCryptHash(testHashString);
        assertThat(testHash.toString()).isEqualTo(testHashString);
    }

}
