package kma.cs.sample.backend.exception;

public class ObjectNotFoundException extends RuntimeException {

    ObjectNotFoundException(final String message) {
        super(message);
    }

}
