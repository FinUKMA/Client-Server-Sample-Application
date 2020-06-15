package kma.cs.sample.domain.packet;

import java.util.ArrayList;
import java.util.List;

import kma.cs.sample.domain.NewProduct;
import kma.cs.sample.domain.Product;
import kma.cs.sample.domain.ProductFilter;
import kma.cs.sample.domain.ProductList;
import lombok.Data;

@Data
public class Command<T> {

    private static final List<Command<?>> ALL_COMMANDS = new ArrayList<>();

    public static final Command<Void> OK = new Command<>(0, Void.class, "OK");
    public static final Command<NewProduct> CREATE_PRODUCT = new Command<>(1, NewProduct.class, "CREATE_PRODUCT");
    public static final Command<Product> GET_PRODUCT_BY_ID = new Command<>(2, Product.class, "GET_PRODUCT_BY_ID");
    public static final Command<ProductFilter> GET_PRODUCTS_LIST = new Command<>(3, ProductFilter.class, "GET_PRODUCTS_LIST");
    public static final Command<ProductList> RESPONSE_PRODUCTS_LIST = new Command<>(4, ProductList.class, "RESPONSE_PRODUCTS_LIST");
    public static final Command<Product> UPDATE_PRODUCT = new Command<>(5, Product.class, "UPDATE_PRODUCT");
    public static final Command<Integer> DELETE_PRODUCT = new Command<>(6, Integer.class, "DELETE_PRODUCT");

    private final int command;
    private final Class<T> klass;
    private final String name;

    private Command(final int command, final Class<T> klass, final String name) {
        this.command = command;
        this.klass = klass;
        this.name = name;

        ALL_COMMANDS.add(this);
    }

    @SuppressWarnings("unchecked")
    public static <T> Command<T> getCommandById(final int cmd) {
        return (Command<T>) ALL_COMMANDS.stream()
            .filter(command -> command.command == cmd)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown command: " + cmd));
    }

}
