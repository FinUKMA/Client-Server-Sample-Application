package kma.cs.sample.desktop;

import java.lang.reflect.Type;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import kma.cs.sample.desktop.exception.LoginException;
import kma.cs.sample.desktop.services.UserService;
import kma.cs.sample.domain.Message;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(event -> System.out.println("Hello World!"));

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();

        try {
            System.out.println(UserService.login("user11", "password"));
        } catch (LoginException e) {
            e.printStackTrace();
        }

        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        stompClient.connect(PropertiesProvider.getString("backend.ws.url"), sessionHandler);
    }

    public static final class MyStompSessionHandler extends StompSessionHandlerAdapter {

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            System.out.println("New session established : " + session.getSessionId());
            session.subscribe("/topic/messages", this);
            session.send("/app/chat", getSampleMessage());
        }

        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
            exception.printStackTrace();
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return Message.class;
        }

        @Override
        public void handleTransportError(final StompSession session, final Throwable exception) {
            exception.printStackTrace();
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            Message msg = (Message) payload;
            System.out.println("Received : " + msg.getText() + " from : " + msg.getFrom());
        }

        /**
         * A sample message instance.
         * @return instance of <code>Message</code>
         */
        private Message getSampleMessage() {
            return new Message("Nicky", "Howdy");
        }
    }

}
