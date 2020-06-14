package kma.cs.sample.backend.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import kma.cs.sample.domain.ByteMessage;
import kma.cs.sample.domain.packet.Decoder;
import kma.cs.sample.domain.packet.Packet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MessagingController {

    @MessageMapping("/execute")
    public void executePacket(final ByteMessage inputMessage) {
        final Packet<?> packet = Decoder.decode(inputMessage.getMessage());
        log.info("Input packet: {}", packet);
    }

}
