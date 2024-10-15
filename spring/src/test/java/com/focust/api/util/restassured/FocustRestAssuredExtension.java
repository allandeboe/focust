/**
 * FocustRestAssuredExtension.java - JUnit 5 Test Extension for REST-Assured.
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
 * This Extension handles the settings for REST-Assured so that requests
 * can work properly.
 *
 * One doesn't need to use this extension directly, as I have already created
 * a custom Annotation to do just that:
 * @see com.focust.api.util.restassured.UseFocustRestAssured
 *
 * @author Allan DeBoe (allan.m.deboe@gmail.com)
 * @version 0.0.3
 * @since 0.0.3
 */
package com.focust.api.util.restassured;

///////////////////////////////////////////////////////////////////////////

// REST-Assured //
import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;

// Apache //
import org.apache.http.conn.ssl.SSLSocketFactory;

// JUnit 5 (Jupiter) //
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

// Spring Framework //
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

// Standard Java //
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.security.KeyStore;
import java.util.Optional;

///////////////////////////////////////////////////////////////////////////

public class FocustRestAssuredExtension implements BeforeEachCallback, TestInstancePostProcessor {

    private ResourceLoader resourceLoader;
    private Environment environment;
    private int serverPort;

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        System.out.println("(FocustRestAssuredExtension - BEFORE-EACH) About to Run \"" + extensionContext.getRequiredTestMethod().getName() + "\"");
        RestAssured.baseURI = "https://localhost:" + serverPort;
        RestAssured.port = serverPort;
        System.out.println("(FocustRestAssuredExtension - BEFORE-EACH) Assigned Base URI & Server Port.");

        Optional<SSLConfig> sslConfig = Optional.ofNullable(getSSLConfig());
        sslConfig.ifPresent(config -> RestAssured.config = RestAssured.config().sslConfig(config));
        if (sslConfig.isEmpty()) {
            System.out.println("(FocustRestAssuredExtension - BEFORE-EACH) ERROR: Unable to assign SSLConfig, as it is null");
            return;
        }
        System.out.println("(FocustRestAssuredExtension - BEFORE-EACH) Assigned SSLConfig");
    }

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext extensionContext) throws Exception {
        Class<?> testClass = extensionContext.getRequiredTestClass();
        System.out.println("(FocustRestAssuredExtension - POST-PROCESS) Current Class: \"" + testClass.getName() + "\".");
        this.serverPort = 8443;

        getServerPort(testInstance, testClass);
        System.out.println("(FocustRestAssuredExtension - POST-PROCESS) Server Port: \"" + this.serverPort + "\".");

        getEnvironment(testInstance, testClass);
        if (this.environment == null) {
            System.out.println("(FocustRestAssuredExtension - POST-PROCESS) ERROR: Unable to find Environment in Test Class.");
            return;
        }
        System.out.println("(FocustRestAssuredExtension - POST-PROCESS) Found Environment in Test Class.");

        getResourceLoader(testInstance, testClass);
        if (this.resourceLoader == null) {
            System.out.println("(FocustRestAssuredExtension - POST-PROCESS) ERROR: Unable to find ResourceLoader in Test Class.");
            return;
        }
        System.out.println("(FocustRestAssuredExtension - POST-PROCESS) Found ResourceLoader in Test Class.");
    }


    private void getServerPort(Object testInstance, Class<?> testClass) throws IllegalAccessException {
        for (Field field : testClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(LocalServerPort.class)) {
                System.out.println("(FocustRestAssuredExtension - POST-PROCESS) Field with @LocalServerPort Annotation: \"" + field.getName() + "\" (" + field.getType().getName() + ").");
                field.setAccessible(true);
                this.serverPort = (Integer)field.get(testInstance);
                field.setAccessible(false);
            }
        }
    }
    private void getEnvironment(Object testInstance, Class<?> testClass) throws IllegalAccessException {
        for (Field field : testClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class) && field.getType().isAssignableFrom(Environment.class)) {
                System.out.println("(FocustRestAssuredExtension - POST-PROCESS) Field with @Autowired Annotation: \"" + field.getName() + "\" (" + field.getType().getName() + ").");
                field.setAccessible(true);
                this.environment = (Environment)field.get(testInstance);
                field.setAccessible(false);
            }
        }
    }
    private void getResourceLoader(Object testInstance, Class<?> testClass) throws IllegalAccessException {
        for (Field field : testClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class) && field.getType().isAssignableFrom(ResourceLoader.class)) {
                System.out.println("(FocustRestAssuredExtension - POST-PROCESS) Field with @Autowired Annotation: \"" + field.getName() + "\" (" + field.getType().getName() + ").");
                field.setAccessible(true);
                this.resourceLoader = (ResourceLoader)field.get(testInstance);
                field.setAccessible(false);
            }
        }
    }

    private SSLConfig getSSLConfig() throws FileNotFoundException {

        Optional<String> keyStoreLocation = Optional.ofNullable(this.environment.getProperty("spring.ssl.bundle.jks.focust-spring.keystore.location"));
        Optional<String> keyStorePassword = Optional.ofNullable(this.environment.getProperty("spring.ssl.bundle.jks.focust-spring.keystore.password"));

        if (keyStoreLocation.isEmpty()) {
            System.out.println("(FocustRestAssuredExtension - BEFORE-EACH) ERROR: \"spring.ssl.bundle.jks.focust-spring.keystore.location\" is null");
            return null;
        }
        if (keyStorePassword.isEmpty()) {
            System.out.println("(FocustRestAssuredExtension - BEFORE-EACH) ERROR: \"spring.ssl.bundle.jks.focust-spring.keystore.password\" is null");
            return null;
        }

        try {
            Resource resource = this.resourceLoader.getResource(keyStoreLocation.get());
            System.out.println("(FocustRestAssuredExtension - BEFORE-EACH) \"spring.ssl.bundle.jks.focust-spring.keystore.location\" is located in \"" + resource.getFile().getPath() + "\"");

            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(
                    new FileInputStream(resource.getFile()), keyStorePassword.get().toCharArray()
            );

            // org.apache.http.conn.ssl.SSLSocketFactory is deprecated,
            // but REST-Assured still requires its use, hence this.
            SSLSocketFactory sslSocketFactory = new SSLSocketFactory(keyStore, keyStorePassword.get());

            SSLConfig sslConfig = new SSLConfig();
            sslConfig.with().sslSocketFactory(sslSocketFactory).and().allowAllHostnames();
            return sslConfig;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
