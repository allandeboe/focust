/**
 * User.java - Model representing the "users" table.
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
 * @version 0.0.3
 * @since 0.0.3
 */
package com.focust.api.users;

///////////////////////////////////////////////////////////////////////////

// Focust //
import com.focust.api.security.bcrypt.BCryptHash;

// Jakarta & Hibernate //
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

// Project Lombok //
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.Type;

// Standard Java //
import java.time.ZonedDateTime;

///////////////////////////////////////////////////////////////////////////

@Entity
@Table(name = "users")
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED) private Long id;

    /**
     * emails are used as usernames for this application.
     *
     * @param email the email of the user.
     * @return email of the user.
     */
    @Column(name = "email", nullable = false, unique = true)
    @NonNull
    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED) private String email;

    /**
     * Since Focust is an issue tracker - something used by developers,
     * it makes sense to store the GitHub username.
     *
     * Also, it will be useful when we integrate GIt and GitHub to the project
     * in future commits.
     *
     * @param githubUsername the username of the user's GitHub account.
     * @return the username of the user's GitHub account.
     */
    @Column(name = "github_username", unique = true)
    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED) private String githubUsername;

    /**
     * User passwords are stored as password hashes as storing user passwords
     * as plaintext is a huge security risk, as all a hacker has to do is to hack
     * into the database and now millions of accounts are compromised.
     *
     * Hashes fixes these problems by doing two things:
     *
     * 1) Hashes are one-way functions: it is easy to create a hash of a string,
     * but nearly impossible to go from a hash to a string. If a hacker does
     * manage to break into the database, they can't log in with the hash stored,
     * but with the string that generated the hash. Since hashes are one way, a
     * hacker cannot undo the hash to get the original password.
     *
     * 2) Hashes are one-to-one, meaning that two hashes are the same if and
     * only if the original strings that generated the hashes are the same.
     * This makes it possible to still verify a user's password when someone
     * logs in.
     *
     * bCrypt hashes are used specifically due to not only including salts, which
     * makes it impossible to hack multiple users that have the same password,
     * but also include a strength value that can be increased to slow down
     * hash generation time, making brute force attacks more tedious.
     *
     * uses the BCryptHash class over String for type correctness
     * @see com.focust.api.security.bcrypt.BCryptHash
     * @see com.focust.api.security.bcrypt.BCryptHashType
     *
     * @param passwordHash the bCrypt hash of the user's password
     * @return the bCrypt hash of the user's password
     */
    @Column(name = "password_hash", nullable = false)
    @Type(value=com.focust.api.security.bcrypt.BCryptHashType.class)
    @NonNull
    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED) private BCryptHash passwordHash;

    /**
     * @param registrationDate the date the user registered their account
     * @return the date the user registered their account
     */
    @Column(name = "registration_date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED) private ZonedDateTime registrationDate;

    protected User() {
        this.registrationDate = ZonedDateTime.now();
    }

}
