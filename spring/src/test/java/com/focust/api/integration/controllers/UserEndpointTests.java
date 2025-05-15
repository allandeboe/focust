/**
 * UserEndpointTests.java - Tests regarding the "/auth" and "/users" endpoints.
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
 * This test simply tests the endpoints corresponding to authentication
 * and users and seeing if the responses are what is expected. This
 * is merely automating what I would do when using Postman to
 * test the endpoints of the back-end server.
 *
 * @see com.focust.api.controllers.AuthenticationController
 * @see com.focust.api.controllers.UserController
 *
 * @author Allan DeBoe (allan.m.deboe@gmail.com)
 * @version 0.0.5
 * @since 0.0.3
 */
package com.focust.api.integration.controllers;

///////////////////////////////////////////////////////////////////////////

// Focust //
import com.focust.api.dto.requests.PageNumberRequest;
import com.focust.api.dto.requests.RegisterUserRequest;
import com.focust.api.dto.requests.SignInUserRequest;
import com.focust.api.util.restassured.UseFocustRestAssured;
import com.focust.api.util.testcontainers.UseFocustMySQL;

// REST-Assured //
import io.restassured.http.ContentType;
import io.restassured.RestAssured;
import io.restassured.http.Cookies;
import io.restassured.response.Response;

// JUnit 5 (Jupiter) //
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

// Spring Framework //
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

// Hamcrest //
import static org.hamcrest.Matchers.isA;

///////////////////////////////////////////////////////////////////////////

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@UseFocustRestAssured
@UseFocustMySQL
@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserEndpointTests {

    // These data members are needed for @UseFocustRestAssured
    @LocalServerPort protected int serverPort;
    @Autowired protected ResourceLoader loader;
    @Autowired protected Environment environment;

    @Test @Order(1)
    public final void givenAuthRegister_whenSendingRequest_thenCreatedStatus() {
        RegisterUserRequest request = new RegisterUserRequest("user@focust.local", "password123");

        System.out.println("(UserEndpointTests) - Sending:\n\"" + request.getJson() + "\"");

        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(request.getJson())
                .when().post("/auth/register");

        String responseBody = response.thenReturn().asString();
        System.out.println("(UserEndpointTests) - Received:\n\"" + responseBody + "\"");

        response.then().assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .cookie("jwt-refresh-token", isA(String.class))
                .and()
                .body("accessToken", isA(String.class))
                .and()
                .body("userId", isA(Integer.class));
    }

    @Test @Order(2)
    public final void givenAuthRegister_whenSendingRequestAndUserExists_thenOkStatus() {
        RegisterUserRequest request = new RegisterUserRequest("user@focust.local", "123456pass");

        System.out.println("(UserEndpointTests) - Sending:\n\"" + request.getJson() + "\"");

        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(request.getJson())
                .when().post("/auth/register");

        String responseBody = response.thenReturn().asString();
        System.out.println("(UserEndpointTests) - Received:\n\"" + responseBody + "\"");

        response.then().assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test @Order(3)
    public final void givenAuthLogin_whenSendingRequest_thenOkStatus() {

        SignInUserRequest request = new SignInUserRequest("user@focust.local", "password123");

        System.out.println("(UserEndpointTests) - Sending:\n\"" + request.getJson() + "\"");

        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(request.getJson())
                .when().post("/auth/login");

        String responseBody = response.thenReturn().asString();
        System.out.println("(UserEndpointTests) - Received:\n\"" + responseBody + "\"");

        response.then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .cookie("jwt-refresh-token", isA(String.class))
                .and()
                .body("accessToken", isA(String.class))
                .and()
                .body("userId", isA(Integer.class));

    }

    @Test @Order(4)
    public final void givenAuthLogin_whenSendingRequestWithWrongPassword_thenUnauthorizedStatus() {

        SignInUserRequest request = new SignInUserRequest("user@focust.local", "123456pass");

        System.out.println("(UserEndpointTests) - Sending:\n\"" + request.getJson() + "\"");

        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(request.getJson())
                .when().post("/auth/login");

        String responseBody = response.thenReturn().asString();
        System.out.println("(UserEndpointTests) - Received:\n\"" + responseBody + "\"");

        response.then().assertThat()
                .statusCode(HttpStatus.UNAUTHORIZED.value());

    }

    @Test @Order(5)
    public final void givenAuthLogin_whenSendingRequestWithNonExistingUser_thenNoContentStatus() {

        SignInUserRequest request = new SignInUserRequest("test@focust.local", "123456pass");

        System.out.println("(UserEndpointTests) - Sending:\n\"" + request.getJson() + "\"");

        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(request.getJson())
                .when().post("/auth/login");

        String responseBody = response.thenReturn().asString();
        System.out.println("(UserEndpointTests) - Received:\n\"" + responseBody + "\"");

        response.then().assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

    }

    @Test @Order(6)
    public final void givenUsers_whenSendingRequestForUsers_thenOkStatus() {

        PageNumberRequest request = new PageNumberRequest();
        System.out.println("(UserEndpointTests) - Sending:\n\"" + request.getJson() + "\"");

        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(request.getJson())
                .when().get("/users");

        String responseBody = response.thenReturn().asString();
        System.out.println("(UserEndpointTests) - Received:\n\"" + responseBody + "\"");

        response.then().assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    /**
     * see UserEndpointTests.givenAuthRegister_whenSendingRequest_thenCreatedStatus
     */
    @Test @Order(7)
    public final void givenRefreshTokenCookie_whenSendingRequestForNewAccessToken_thenOkStatus() {

        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("{}")
                .when().get("/auth/refresh");

        String responseBody = response.thenReturn().asString();
        System.out.println("(UserEndpointTests) - Received:\n\"" + responseBody + "\"");

        response.then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .cookie("jwt-refresh-token", isA(String.class))
                .and()
                .body("accessToken", isA(String.class))
                .and()
                .body("userId", isA(Integer.class));
    }

}
