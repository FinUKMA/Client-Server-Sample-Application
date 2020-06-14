package kma.cs.sample.desktop.ui;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
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
import kma.cs.sample.desktop.websocket.WebSocketMessageHandler;

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
            StompSessionHandler sessionHandler = new WebSocketMessageHandler();

            stompClient.connect(PropertiesProvider.getString("backend.ws.url"), sessionHandler);
        } catch (final LoginException ex) {
            messageField.setText(ex.getErrorResponse().getMessage());
        }
    }

}
