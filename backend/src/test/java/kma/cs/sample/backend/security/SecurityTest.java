package kma.cs.sample.backend.security;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.text.IsEmptyString.emptyOrNullString;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import kma.cs.sample.backend.AbstractSpringBootTest;
import kma.cs.sample.domain.UserCredentialsDto;

@DatabaseTearDown("/tearDown.xml")
@DatabaseSetup("/SecurityTest/initData.xml")
class SecurityTest extends AbstractSpringBootTest {

    @Test
    void shouldPassLoginAndGetValidToken() {
        given()
            .body(UserCredentialsDto.of("user1", "password"))
        .when()
            .post("/login")
        .then()
            .statusCode(200)
            .body("accessToken", not(emptyOrNullString()))
            .body("login", is("user1"))
            .body("fullName", is("fullName1"));
    }

    @ParameterizedTest
    @CsvSource({
        "unknown_login,any_password",
        "user1,invalid_password",
    })
    void shouldReturn400_whenInvalidCredentials(final String login, final String password) {
        given()
            .body(UserCredentialsDto.of(login, password))
        .when()
            .post("/login")
        .then()
            .statusCode(400)
            .body("message", is("Wrong credentials"));
    }

}