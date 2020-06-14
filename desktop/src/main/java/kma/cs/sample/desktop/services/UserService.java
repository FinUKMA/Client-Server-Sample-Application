package kma.cs.sample.desktop.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kma.cs.sample.desktop.PropertiesProvider;
import kma.cs.sample.domain.request.UserCredentialsDto;
import kma.cs.sample.domain.user.AuthenticatedUserDto;

public class UserService {

    public static AuthenticatedUserDto login(final String login, final String password) throws JsonProcessingException {
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

        return objectMapper.readValue(response, AuthenticatedUserDto.class);
    }

}
