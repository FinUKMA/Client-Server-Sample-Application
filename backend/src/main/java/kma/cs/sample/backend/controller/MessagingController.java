package kma.cs.sample.backend.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import kma.cs.sample.backend.service.CommandHandlerService;
import kma.cs.sample.domain.ByteMessage;
import kma.cs.sample.domain.packet.Decoder;
import kma.cs.sample.domain.packet.Encoder;
import kma.cs.sample.domain.packet.Packet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessagingController {

    private final CommandHandlerService handlerService;

    @MessageMapping("/execute")
    @SendTo("/topic/process-ok")
    public ByteMessage executePacket(final ByteMessage inputMessage) {
        final Packet<?> packet = Decoder.decode(inputMessage.getMessage());
        log.info("Input packet: {}", packet);

        return new ByteMessage(Encoder.encode(handlerService.handle(packet)));
    }

}
