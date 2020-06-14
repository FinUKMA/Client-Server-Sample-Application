package kma.cs.sample.backend.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kma.cs.sample.domain.packet.Command;
import kma.cs.sample.domain.packet.Packet;

@Component
public class CommandHandlerService {

    private final Map<Command<?>, CommandHandler<?>> handlersByCommand;

    public CommandHandlerService(final List<CommandHandler<?>> commands) {
        this.handlersByCommand = commands.stream()
            .collect(Collectors.toUnmodifiableMap(CommandHandler::getCommand, Function.identity()));
    }

    @SuppressWarnings("unchecked")
    public <T> Packet<?> handle(final Packet<T> packet) {
        final CommandHandler<T> handler = (CommandHandler<T>) handlersByCommand.get(packet.getCommand());
        if (handler == null) {
            throw new RuntimeException("No handler for command: " + packet.getCommand());
        }
        return handler.handle(packet);
    }

}
