package kma.cs.sample.desktop.ui.products;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ListProductsWindow implements Initializable {

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
    }

    public void addNewProductWindow() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ui/products/add-new.fxml"));
        Stage stage = new Stage();
        stage.setTitle("New Product");
        stage.setScene(new Scene(root, 450, 450));
        stage.show();
    }
}
