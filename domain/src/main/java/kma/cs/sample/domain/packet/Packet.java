package kma.cs.sample.domain.packet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data(staticConstructor = "of")
@Builder(builderClassName = "Builder")
@NoArgsConstructor
@AllArgsConstructor
public class Packet<T> {

    private long packetId;
    private Command<T> command;
    private T body;
    private int userId;

}
