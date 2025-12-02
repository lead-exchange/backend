package lead.exchange.schedule;

import java.util.List;
import lead.exchange.entity.User;
import lead.exchange.samolet.TopnlabApiService;
import lead.exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EstateFillScheduler {

    private static final int BATCH_SIZE = 500;
    private static final long INITIAL_DELAY = 1000L;
    private static final long FIXED_RATE = 3_600_000L; // 1 hour

    private final TopnlabApiService topnlabApiService;
    private final UserService userService;

    @Scheduled(fixedRate = FIXED_RATE, initialDelay = INITIAL_DELAY)
    void fillEstates() {
        long offset = 0;
        log.info("Start fill users with estates");

        while (true) {
            List<User> all = userService.getAll(offset, BATCH_SIZE);

            if (all.isEmpty()) {
                break;
            }

            all.forEach(user -> topnlabApiService.updateEstates(user.getId(), user.getPhone()));

            offset += BATCH_SIZE;
        }

        log.info("Finish fill users with estates");

    }
}
