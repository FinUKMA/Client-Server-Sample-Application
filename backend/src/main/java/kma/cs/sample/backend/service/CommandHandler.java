package kma.cs.sample.backend.service;

import kma.cs.sample.domain.packet.Command;
import kma.cs.sample.domain.packet.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class CommandHandler<T> {

    @Getter
    private final Command<T> command;

    public abstract Packet<?> handle(final Packet<T> packet);

}
