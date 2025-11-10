package lead.exchange.service;

import lead.exchange.entity.Estate;
import lead.exchange.repository.EstateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EstateService {
    private final EstateRepository estateRepository;
    private final UserService userService;

    public List<Estate> getEstateByUserId(UUID userId){
        userService.checkUserExistByUserid(userId);
        return estateRepository.findByUserId(userId);
    }
}
