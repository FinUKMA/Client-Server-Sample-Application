package kma.cs.sample.backend.exception;

public class ProductNotFoundException extends ObjectNotFoundException {

    public ProductNotFoundException(final int id) {
        super("No product with id: " + id);
    }

}
