package kma.cs.sample.backend.config;

import static java.lang.Boolean.TRUE;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.HEAD;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import kma.cs.sample.backend.security.InitialJwtAuthenticationFilter;
import kma.cs.sample.backend.security.JwtTokenProvider;
import kma.cs.sample.backend.security.SecurityConfig;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Import(SecurityConfig.class)
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationProvider authenticationProvider;
    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Stream.of(HEAD, GET, POST, PUT, DELETE)
            .map(Enum::name)
            .collect(toUnmodifiableList()));
        configuration.setAllowCredentials(TRUE);
        configuration.setAllowedHeaders(List.of(AUTHORIZATION, CACHE_CONTROL, CONTENT_TYPE));
        configuration.setExposedHeaders(List.of(AUTHORIZATION));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf()
            .disable()
            .cors()
        .and()
            .authorizeRequests()
            .anyRequest().permitAll()
        .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .addFilter(jwtAuthenticationFilter());
    }

    @SneakyThrows
    @Override
    public AuthenticationManager authenticationManagerBean() {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }


    private InitialJwtAuthenticationFilter jwtAuthenticationFilter() {
        return new InitialJwtAuthenticationFilter(authenticationManagerBean(), objectMapper, jwtTokenProvider);
    }

}