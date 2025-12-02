package lead.exchange.schedule;

import java.util.List;
import lead.exchange.entity.User;
import lead.exchange.repository.UserRepository;
import lead.exchange.samolet.TopnlabApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EstateFillScheduler {

    private final UserRepository userRepository;
    private final TopnlabApiService topnlabApiService;

    @Scheduled(initialDelay = 1000, fixedDelay = 5)
    void fillEstates() {
        log.info("Start fill users with estates");
        List<User> all = userRepository.findAll();

        all.forEach(user -> topnlabApiService.updateEstates(user.getId(), user.getPhone()));

    }
}
