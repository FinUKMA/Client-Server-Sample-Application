package kma.cs.sample.backend.security;

import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import kma.cs.sample.backend.domain.AuthenticatedUser;
import kma.cs.sample.backend.exception.UserNotFoundException;
import kma.cs.sample.domain.request.UserCredentials;
import kma.cs.sample.domain.response.ErrorResponse;
import kma.cs.sample.domain.user.AuthenticatedUserDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InitialJwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public InitialJwtAuthenticationFilter(
        final AuthenticationManager authenticationManager,
        final ObjectMapper objectMapper,
        final JwtTokenProvider jwtTokenProvider
    ) {
        super();
        this.objectMapper = objectMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        setAuthenticationManager(authenticationManager);
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) {
        final UserCredentials userCredentials = objectMapper.readValue(request.getInputStream(), UserCredentials.class);

        final Authentication authToken =
            new UsernamePasswordAuthenticationToken(userCredentials.getLogin(), userCredentials.getPassword(), List.of());

        return getAuthenticationManager().authenticate(authToken);
    }

    @SneakyThrows
    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
        final FilterChain chain, final Authentication auth) {

        final AuthenticatedUser authenticatedUser = (AuthenticatedUser) auth.getPrincipal();
        final String token = jwtTokenProvider.generateToken(authenticatedUser);

        log.debug("Generate token {} from user with login {}", token, authenticatedUser.getUsername());

        final AuthenticatedUserDto authenticatedUserDto = AuthenticatedUserDto.builder()
            .login(authenticatedUser.getUsername())
            .fullName(authenticatedUser.getFullName())
            .build();

        response.addHeader(AUTHORIZATION_HEADER, token);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(authenticatedUserDto));
    }

    @SneakyThrows
    @Override
    protected void unsuccessfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException failed) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(authenticationExceptionHandler(failed)));
    }

    private ErrorResponse authenticationExceptionHandler(final AuthenticationException ex) {
        if (ex instanceof BadCredentialsException || ex.getCause() instanceof UserNotFoundException) {
            return ErrorResponse.of("Wrong credentials");
        }

        return ErrorResponse.of(ex.getMessage());
    }
}
