package kma.cs.sample.backend.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import kma.cs.sample.domain.Message;

@Controller
public class MessagingController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message send(final Message message) {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new Message(message.getFrom(), message.getText() + " " + time);
    }

}
