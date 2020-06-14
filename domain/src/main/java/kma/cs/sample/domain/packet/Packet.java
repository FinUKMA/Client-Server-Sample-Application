package kma.cs.sample.domain.packet;

import lombok.Builder;
import lombok.Data;

@Data(staticConstructor = "of")
@Builder(builderClassName = "Builder")
public class Packet<T> {

    private final long packetId;
    private final Command<T> command;
    private final T body;
    private final int userId;

}
