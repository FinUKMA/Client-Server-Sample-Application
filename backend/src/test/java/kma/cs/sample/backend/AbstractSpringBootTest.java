package kma.cs.sample.backend;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.restassured.RestAssured;
import kma.cs.sample.backend.dao.util.DbUnitTestListeners;

@DbUnitTestListeners
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractSpringBootTest {

    @LocalServerPort
    private void initLocalServerPort(final int localServerPort) {
        RestAssured.port = localServerPort;
    }

}

