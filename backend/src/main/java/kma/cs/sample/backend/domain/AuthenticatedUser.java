package kma.cs.sample.backend.domain;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.With;
import lombok.experimental.Delegate;

@With
@Builder(builderClassName = "Builder")
@Value
public class AuthenticatedUser implements UserDetails {

    @Delegate
    @NonNull
    private final UserDetails delegate;

    @NonNull
    private final String fullName;

}