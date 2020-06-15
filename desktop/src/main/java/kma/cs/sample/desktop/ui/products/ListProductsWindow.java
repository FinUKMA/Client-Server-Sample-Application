package kma.cs.sample.desktop.ui.products;

import java.io.IOException;
import java.math.BigDecimal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kma.cs.sample.desktop.GlobalContext;
import kma.cs.sample.domain.Product;
import kma.cs.sample.domain.ProductFilter;
import kma.cs.sample.domain.ProductList;
import kma.cs.sample.domain.packet.Command;
import kma.cs.sample.domain.packet.Packet;

public class ListProductsWindow {

    @FXML
    private TableView<Product> productsTable;
    @FXML
    private TextField nameFilter;
    @FXML
    private TextField priceFromFilter;
    @FXML
    private TextField priceToFilter;
    @FXML
    private TextField totalFromFilter;
    @FXML
    private TextField totalToFilter;

    @FXML
    public void initialize() {
        reloadProducts();
    }

    public void addNewProductWindow() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ui/products/add-new.fxml"));
        Stage stage = new Stage();
        stage.setTitle("New Product");
        stage.setScene(new Scene(root, 450, 450));
        stage.show();
    }

    @SuppressWarnings("unchecked")
    public void reloadProducts() {
        final ProductFilter productFilter = ProductFilter.builder()
            .name(nameFilter.getText())
            .priceFrom(parseBigDecimal(priceFromFilter))
            .priceTo(parseBigDecimal(priceToFilter))
            .totalFrom(parseBigDecimal(totalFromFilter))
            .totalTo(parseBigDecimal(totalToFilter))
            .page(0)
            .size(200)
            .build();

        GlobalContext.WEB_SOCKET.send(Command.GET_PRODUCTS_LIST, productFilter, response -> {
            System.out.println("get product list response: " + response);

            if (response.getCommand() == Command.RESPONSE_PRODUCTS_LIST) {
                final Packet<ProductList> productListPacket = (Packet<ProductList>) response;
                productsTable.getItems().clear();
                productsTable.getItems().addAll(productListPacket.getBody().getProducts());
            } else {
                System.out.println("unexpected response");
            }
        });
    }

    private static BigDecimal parseBigDecimal(final TextField textField) {
        return textField.getText().isEmpty() ? null : new BigDecimal(textField.getText());
    }
}
