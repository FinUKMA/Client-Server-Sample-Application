package kma.cs.sample.backend.dao.util;

import java.util.List;

import org.assertj.core.util.Lists;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class SequenceResetTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestMethod(final TestContext testContext) throws Exception {
        final JdbcTemplate jdbcTemplate = testContext.getApplicationContext().getBean(JdbcTemplate.class);
        final List<String> batchUpdate = Lists.newArrayList();
        jdbcTemplate.queryForList("SELECT * FROM INFORMATION_SCHEMA.SEQUENCES")
            .forEach(sequence -> batchUpdate.add("ALTER SEQUENCE " + sequence.get("SEQUENCE_NAME") + " RESTART WITH 1000"));
        if (!batchUpdate.isEmpty()) {
            jdbcTemplate.batchUpdate(batchUpdate.toArray(new String[0]));
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
