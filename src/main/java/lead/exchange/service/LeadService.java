package lead.exchange.service;

import lead.exchange.entity.Lead;
import lead.exchange.repository.LeadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LeadService {
    private final LeadRepository leadRepository;
    private final UserService userService;

    public List<Lead> findByUserId(UUID userId){
        userService.checkUserExistByUserid(userId);
        return leadRepository.findByUserId(userId);
    }
}
