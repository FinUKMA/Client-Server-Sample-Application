package kma.cs.sample.desktop.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import kma.cs.sample.desktop.PropertiesProvider;
import kma.cs.sample.desktop.exception.LoginException;
import kma.cs.sample.domain.UserCredentialsDto;
import kma.cs.sample.domain.ErrorResponseDto;
import kma.cs.sample.domain.AuthenticatedUserDto;

public class UserService {

    public static AuthenticatedUserDto login(final String login, final String password) throws LoginException {
        try {
            final HttpClient httpClient = HttpClient.newHttpClient();
            final ObjectMapper objectMapper = new ObjectMapper();
            final String requestBody = objectMapper.writeValueAsString(UserCredentialsDto.of(login, password));

            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(PropertiesProvider.getString("backend.login.url")))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            final String response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();

            try {
                return objectMapper.readValue(response, AuthenticatedUserDto.class);
            } catch (final UnrecognizedPropertyException ex) {
                throw new LoginException(objectMapper.readValue(response, ErrorResponseDto.class));
            }
        } catch (final JsonProcessingException ex) {
            throw new RuntimeException("Can't process JSON", ex);
        }
    }

}
