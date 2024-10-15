/**
 * BCryptHashType.java - Hibernate UserType for bCrypt hashes
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
 * Since Hibernate does not recognize "BCryptHash", a custom Hibernate
 * UserType is created to allow this immutable class be used.
 *
 * @see com.focust.api.security.bcrypt.BCryptHash
 *
 * @author Allan DeBoe (allan.m.deboe@gmail.com)
 * @version 0.0.3
 * @since 0.0.3
 */
package com.focust.api.security.bcrypt;

///////////////////////////////////////////////////////////////////////////

// Hibernate //
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

// Standard Java //
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;
import java.util.Optional;

///////////////////////////////////////////////////////////////////////////

public final class BCryptHashType implements UserType<BCryptHash> {

    @Override
    public int getSqlType() {
        return Types.VARCHAR;
    }

    @Override
    public Class<BCryptHash> returnedClass() {
        return BCryptHash.class;
    }

    @Override
    public boolean equals(BCryptHash hash1, BCryptHash hash2) {
        return Objects.equals(hash1, hash2);
    }

    @Override
    public int hashCode(BCryptHash hash) {
        return hash.hashCode();
    }

    @Override
    public BCryptHash nullSafeGet(ResultSet resultSet, int index, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws SQLException {
        Optional<String> hashString = Optional.ofNullable(resultSet.getString(index));
        return hashString.map(BCryptHash::new).orElse(null);
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, BCryptHash hash, int index, SharedSessionContractImplementor sharedSessionContractImplementor) throws SQLException {
        Optional<String> hashString = Optional.ofNullable(hash).map(BCryptHash::toString);
        if (hashString.isEmpty()) {
            preparedStatement.setNull(index, Types.VARCHAR);
            return;
        }
        preparedStatement.setString(index, hashString.get());
    }

    @Override
    public BCryptHash deepCopy(BCryptHash hash) {
        return hash;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(BCryptHash hash) {
        // BCryptHash is already Serializable
        return hash;
    }

    @Override
    public BCryptHash assemble(Serializable serializable, Object o) {
        return new BCryptHash(serializable.toString());
    }

}
