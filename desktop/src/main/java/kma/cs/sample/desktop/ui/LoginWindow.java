package kma.cs.sample.desktop.ui;

import java.io.IOException;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kma.cs.sample.desktop.GlobalContext;
import kma.cs.sample.desktop.PropertiesProvider;
import kma.cs.sample.desktop.exception.LoginException;
import kma.cs.sample.desktop.services.UserService;

public class LoginWindow {

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField messageField;

    public void processLogin() throws IOException {
        System.out.println("Process login");

        try {
            GlobalContext.AUTHENTICATED_USER = UserService.login(loginField.getText(), passwordField.getText());
            messageField.setText("Logged in successfully");

            WebSocketClient client = new StandardWebSocketClient();
            WebSocketStompClient stompClient = new WebSocketStompClient(client);
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());

            stompClient.connect(PropertiesProvider.getString("backend.ws.url"), GlobalContext.WEB_SOCKET);

            FXMLLoader loader = new FXMLLoader();
            Stage stage = (Stage) loginField.getScene().getWindow();
            VBox root = loader.load(getClass().getResourceAsStream("/ui/products/list.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (final LoginException ex) {
            messageField.setText(ex.getErrorResponse().getMessage());
        }
    }

}
