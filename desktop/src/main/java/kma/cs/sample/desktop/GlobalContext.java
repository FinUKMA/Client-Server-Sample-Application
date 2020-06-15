package kma.cs.sample.desktop;

import java.util.concurrent.atomic.AtomicLong;

import kma.cs.sample.desktop.websocket.WebSocketMessageHandler;
import kma.cs.sample.domain.AuthenticatedUserDto;

public class GlobalContext {

    public static AuthenticatedUserDto AUTHENTICATED_USER;
    public static WebSocketMessageHandler WEB_SOCKET = new WebSocketMessageHandler();
    public static final AtomicLong PACKET_ID = new AtomicLong(0);

}
