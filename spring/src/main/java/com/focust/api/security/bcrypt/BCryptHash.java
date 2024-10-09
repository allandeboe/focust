/**
 * BCryptHash.java - Immutable Object for BCrypt Hashes
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
 * This Immutable class is used to make it rather clear that a String
 * is indeed a bCrypt hash. Used for type correctness.
 *
 * Since Hibernate does not recognize "BCryptHash", a custom Hibernate
 * UserType is created to allow this immutable class be used.
 *
 * @see com.focust.api.security.bcrypt.BCryptHashType
 *
 * @author Allan DeBoe (allan.m.deboe@gmail.com)
 * @version 0.0.3
 * @since 0.0.3
 */
package com.focust.api.security.bcrypt;

///////////////////////////////////////////////////////////////////////////

// Spring Framework //
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Standard Java //
import java.io.Serializable;
import java.util.StringTokenizer;

///////////////////////////////////////////////////////////////////////////

public final class BCryptHash implements Serializable {

    final BCryptPasswordEncoder.BCryptVersion version;
    final int strength; // a.k.a. input cost
    final String salt;
    final String hash;

    public BCryptHash(String hashString) throws IllegalArgumentException {

        StringTokenizer tokenizer = new StringTokenizer(hashString, "$");
        if (tokenizer.countTokens() != 3) {
            throw new IllegalArgumentException("String cannot be converted to a bCrypt hash");
        }

        String version = tokenizer.nextToken();
        switch (version) {
            case "2a" -> this.version = BCryptPasswordEncoder.BCryptVersion.$2A;
            case "2b" -> this.version = BCryptPasswordEncoder.BCryptVersion.$2B;
            case "2y" -> this.version = BCryptPasswordEncoder.BCryptVersion.$2Y;
            default -> throw new IllegalArgumentException("Invalid bCrypt version; only accepts '2a', '2b', or '2y'");
        }

        this.strength = Integer.parseInt(tokenizer.nextToken());

        final String lastToken = tokenizer.nextToken();
        if (lastToken.length() != 53) throw new IllegalArgumentException("");
        this.salt = lastToken.substring(0, 22);
        this.hash = lastToken.substring(22);

    }

    @Override
    public String toString() {
        return "$" + this.version.getVersion() + "$" + this.strength + "$" + this.salt + this.hash;
    }

}
