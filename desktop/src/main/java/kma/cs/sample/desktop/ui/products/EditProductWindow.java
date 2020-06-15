package kma.cs.sample.desktop.ui.products;

import java.math.BigDecimal;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import kma.cs.sample.desktop.GlobalContext;
import kma.cs.sample.domain.ErrorResponseDto;
import kma.cs.sample.domain.Product;
import kma.cs.sample.domain.packet.Command;
import kma.cs.sample.domain.packet.Packet;

public class EditProductWindow {

    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField totalField;
    @FXML
    private TextField messageField;

    private Product product;

    public void receiveProduct(final Product product) {
        this.product = product;
        nameField.setText(product.getName());
        priceField.setText(product.getPrice().toString());
        totalField.setText(product.getTotal().toString());
    }

    @SuppressWarnings("unchecked")
    public void saveProduct() {
        product.setName(nameField.getText());
        product.setPrice(new BigDecimal(priceField.getText()));
        product.setTotal(new BigDecimal(totalField.getText()));
        System.out.println("Update product: " + product);

        GlobalContext.WEB_SOCKET.send(Command.UPDATE_PRODUCT, product, response -> {
            if (response.getCommand() == Command.OK) {
                messageField.setText("Product update");
            } else if (response.getCommand() == Command.ERROR) {
                final Packet<ErrorResponseDto> errorResponse = (Packet<ErrorResponseDto>) response;
                messageField.setText("Failed to update product. Reason: " + errorResponse.getBody().getMessage());
            } else {
                messageField.setText("Failed to update product");
            }
        });
    }
}
