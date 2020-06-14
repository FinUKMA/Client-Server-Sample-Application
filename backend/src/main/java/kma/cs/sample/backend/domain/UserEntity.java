package kma.cs.sample.backend.domain;

import lombok.Data;

@Data(staticConstructor = "of")
public class UserEntity {

    private final int id;
    private final String login;
    private final String password;
    private final String fullName;

}
