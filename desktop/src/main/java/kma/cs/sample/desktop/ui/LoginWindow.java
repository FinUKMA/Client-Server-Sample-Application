package kma.cs.sample.desktop.ui;

import java.math.BigDecimal;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import kma.cs.sample.desktop.GlobalContext;
import kma.cs.sample.desktop.PropertiesProvider;
import kma.cs.sample.desktop.exception.LoginException;
import kma.cs.sample.desktop.services.UserService;
import kma.cs.sample.domain.Product;
import kma.cs.sample.domain.packet.Command;

public class LoginWindow {

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField messageField;

    public void processLogin() {
        System.out.println("Process login");

        try {
            GlobalContext.AUTHENTICATED_USER = UserService.login(loginField.getText(), passwordField.getText());
            messageField.setText("Logged in successfully");

            WebSocketClient client = new StandardWebSocketClient();
            WebSocketStompClient stompClient = new WebSocketStompClient(client);
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());

            stompClient.connect(PropertiesProvider.getString("backend.ws.url"), GlobalContext.WEB_SOCKET);

            GlobalContext.WEB_SOCKET.send(Command.CREATE_PRODUCT, Product.of("name1", BigDecimal.ONE, BigDecimal.TEN), response -> {
                System.out.println("create packet response: " + response);
            });
        } catch (final LoginException ex) {
            messageField.setText(ex.getErrorResponse().getMessage());
        }
    }

}
