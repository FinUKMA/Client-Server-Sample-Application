package kma.cs.sample.backend.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import kma.cs.sample.backend.domain.User;
import kma.cs.sample.backend.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public User getUserByLogin(final String login) {
        final User user = jdbcTemplate.query("select * from users where login = ?", DaoUtils.toArray(login), UserDao::userRowMapper);
        if (user == null) {
            throw new UserNotFoundException(login);
        }
        return user;
    }

    private static User userRowMapper(final ResultSet resultSet) throws SQLException {
        return resultSet.next()
            ? User.of(resultSet.getInt("id"), resultSet.getString("login"), resultSet.getString("password"), resultSet.getString("full_name"))
            : null;
    }

}
