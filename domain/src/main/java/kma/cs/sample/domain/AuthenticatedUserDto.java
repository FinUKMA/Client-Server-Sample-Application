package kma.cs.sample.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "Builder")
public class AuthenticatedUserDto {

    private String accessToken;
    private String login;
    private String fullName;
    private UserRole role;

}
