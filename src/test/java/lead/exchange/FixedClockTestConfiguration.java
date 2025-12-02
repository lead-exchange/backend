package lead.exchange;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class FixedClockTestConfiguration {

    @Bean
    public Clock fixedTestClock() {
        return new TestClockProxy();
    }

    public static class TestClockProxy extends Clock {

        private static volatile Clock CLOCK;

        public TestClockProxy() {
            super();
            setToFixedClock();
        }

        public static void setToFixedClock() {
            CLOCK = Clock.fixed(
                Instant.now().minusSeconds(60L).truncatedTo(ChronoUnit.SECONDS),
                ZoneId.systemDefault()
            );
        }

        @Override
        public ZoneId getZone() {
            return CLOCK.getZone();
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return CLOCK.withZone(zone);
        }

        @Override
        public Instant instant() {
            return CLOCK.instant();
        }
    }
}
