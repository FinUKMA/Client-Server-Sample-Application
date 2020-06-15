package kma.cs.sample.desktop.ui.products;

import java.math.BigDecimal;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import kma.cs.sample.desktop.GlobalContext;
import kma.cs.sample.domain.NewProduct;
import kma.cs.sample.domain.Product;
import kma.cs.sample.domain.packet.Command;
import kma.cs.sample.domain.packet.Packet;

public class NewProductWindow {

    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField messageField;

    @SuppressWarnings("unchecked")
    public void saveNewProduct() {
        final NewProduct newProduct = NewProduct.of(nameField.getText(), new BigDecimal(priceField.getText()));
        System.out.println("Create new product: " + newProduct);

        GlobalContext.WEB_SOCKET.send(Command.CREATE_PRODUCT, newProduct, response -> {
            if (response.getCommand() == Command.GET_PRODUCT_BY_ID) {
                final Packet<Product> getProductByIdPacket = (Packet<Product>) response;
                System.out.println("New product: " + response);
                messageField.setText("New product created. ID: " + getProductByIdPacket.getBody().getId());
            } else {
                messageField.setText("Failed to create new product");
            }
        });
    }
}
