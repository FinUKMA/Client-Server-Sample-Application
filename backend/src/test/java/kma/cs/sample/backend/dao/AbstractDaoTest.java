package kma.cs.sample.backend.dao;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import kma.cs.sample.backend.dao.util.DbUnitTestListeners;

@JdbcTest
@ExtendWith(SpringExtension.class)
@DbUnitTestListeners
@ComponentScan(basePackageClasses = UserDao.class)
public class AbstractDaoTest {

}
