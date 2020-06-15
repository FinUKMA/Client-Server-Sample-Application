package kma.cs.sample.backend.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import kma.cs.sample.backend.dao.UserDao;
import kma.cs.sample.backend.domain.AuthenticatedUser;
import kma.cs.sample.backend.domain.UserEntity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DaoUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(final String login) throws UsernameNotFoundException {
        final UserEntity user = userDao.getUserByLogin(login);

        final UserDetails userDetails = User.builder()
            .username(user.getLogin())
            .password(user.getPassword())
            .disabled(false)
            .credentialsExpired(false)
            .accountLocked(false)
            .accountExpired(false)
            .authorities(user.getRole().name())
            .build();

        return AuthenticatedUser.builder()
            .delegate(userDetails)
            .fullName(user.getFullName())
            .build();
    }
}
