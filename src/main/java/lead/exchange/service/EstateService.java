package lead.exchange.service;

import java.util.List;
import java.util.UUID;
import lead.exchange.entity.Estate;
import lead.exchange.repository.EstateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstateService {
    private final EstateRepository estateRepository;
    private final UserService userService;

    public List<Estate> getEstateByUserId(UUID userId) {
        userService.checkUserExistByUserid(userId);
        return estateRepository.findByUserId(userId);
    }
    public List<Estate> getAllEstate(){
        return estateRepository.findAll();
    }
}
