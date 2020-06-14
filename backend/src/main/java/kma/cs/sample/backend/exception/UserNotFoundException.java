package kma.cs.sample.backend.exception;

public class UserNotFoundException extends ObjectNotFoundException {

    public UserNotFoundException(final String login) {
        super("No user with login: " + login);
    }

}
