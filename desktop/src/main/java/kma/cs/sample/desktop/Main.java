package kma.cs.sample.desktop;

import java.io.IOException;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kma.cs.sample.desktop.websocket.WebSocketMessageHandler;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            VBox root = loader.load(Main.class.getResourceAsStream("/ui/login-window.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Warehouse");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new WebSocketMessageHandler();

        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        headers.add("receiptId", "1");

        stompClient.connect(PropertiesProvider.getString("backend.ws.url"), headers, sessionHandler);
    }

}
