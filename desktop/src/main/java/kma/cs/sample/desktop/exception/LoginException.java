package kma.cs.sample.desktop.exception;

import kma.cs.sample.domain.ErrorResponseDto;
import lombok.Getter;

public class LoginException extends Exception {

    @Getter
    private final ErrorResponseDto errorResponse;

    public LoginException(final ErrorResponseDto errorResponse) {
        super("Login request failed. " + errorResponse);
        this.errorResponse = errorResponse;
    }

}
