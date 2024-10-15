/**
 * JWTService.java - Service that handles JWT Tokens
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
package com.focust.api.security.jwt;

///////////////////////////////////////////////////////////////////////////

// Auth0 Java JWT //
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

// Focust //
import com.auth0.jwt.interfaces.DecodedJWT;
import com.focust.api.users.UserJWTDetails;

// Spring Framework //
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

// Standard Java //
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

///////////////////////////////////////////////////////////////////////////

@Service
public class JWTService {

    private final static String issuer = "focust";
    private final static long accessTokenExpirationTime = 5 * 60;

    @Autowired
    private Environment environment;

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * @param userDetails a UserJWTDetails object containing relevant details of the user
     * @return An Optional<String> object that contains either nothing or the newly token token
     * @throws NoSuchAlgorithmException or InvalidKeySpecException if JWTService incorrectly extracts the Public and/or Private Keys.
     */
    public final Optional<String> generateAccessToken(UserJWTDetails userDetails) throws NoSuchAlgorithmException, InvalidKeySpecException {
        try {
            Instant currentTime = Instant.now();
            return Optional.ofNullable(JWT.create()
                    .withIssuer(JWTService.issuer)
                    .withClaim("email", userDetails.getEmail())
                    .withExpiresAt(Date.from(currentTime.plusSeconds(accessTokenExpirationTime)))
                    .withIssuedAt(Date.from(currentTime))
                    .sign(Algorithm.RSA256(this.getPublicKey(), this.getPrivateKey())));
        }
        catch (JWTCreationException | IOException e) {
            System.out.println("(JWTService - generateAccessToken) ERROR: \"" + e.getMessage() + "\"");
            return Optional.empty();
        }
    }

    /**
     * @param jwtToken a String representing the JWT token.
     * @return an Optional<String> representing the email
     * @throws NoSuchAlgorithmException or InvalidKeySpecException if JWTService incorrectly extracts the Public and/or Private Keys.
     */
    public final Optional<String> getEmail(String jwtToken) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (jwtToken.isEmpty()) return Optional.empty();
        try {
            DecodedJWT validatedToken = this.getValidatedToken(jwtToken);
            return Optional.ofNullable(validatedToken.getClaim("email").toString());
        }
        catch (JWTVerificationException | IOException e) {
            return Optional.empty();
        }
    }

    ///////////////////////////////////////////////////////////////////////////

    private DecodedJWT getValidatedToken(String jwtToken) throws IOException, JWTVerificationException, NoSuchAlgorithmException, InvalidKeySpecException {
        JWTVerifier verifier = JWT.require(Algorithm.RSA256(this.getPublicKey(), this.getPrivateKey()))
                .withIssuer(JWTService.issuer)
                .build();
        return verifier.verify(jwtToken.replace("Bearer ", ""));
    }

    private RSAPublicKey getPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        File public_key_file = this.resourceLoader.getResource(Objects.requireNonNull(environment.getProperty("jwt.rsa.public-key"))).getFile();
        byte[] decoded_public_key =  Files.readAllBytes(public_key_file.toPath());
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded_public_key));
    }

    private RSAPrivateKey getPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        File private_key_file = this.resourceLoader.getResource(Objects.requireNonNull(environment.getProperty("jwt.rsa.private-key"))).getFile();
        byte[] decoded_private_key = Files.readAllBytes(private_key_file.toPath());
        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded_private_key));
    }

}
