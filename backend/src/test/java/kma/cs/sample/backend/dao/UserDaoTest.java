package kma.cs.sample.backend.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import kma.cs.sample.backend.domain.UserEntity;
import kma.cs.sample.backend.exception.UserNotFoundException;

@DatabaseTearDown("/tearDown.xml")      // clean-up database or specific tables after each test
@DatabaseSetup("/UserDaoTest/initData.xml")     // initialize database before each test
class UserDaoTest extends AbstractDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    void shouldThrowException_whenNoUserByLogin() {
        assertThatThrownBy(() -> userDao.getUserByLogin("unknown_user"))
            .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void shouldFindUserByLogin() {
        assertThat(userDao.getUserByLogin("login1"))
            .extracting(
                UserEntity::getId,
                UserEntity::getLogin,
                UserEntity::getFullName
            )
            .containsExactly(
                1,
                "login1",
                "fullName1"
            );
    }

}