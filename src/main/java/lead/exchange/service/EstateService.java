package lead.exchange.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lead.exchange.dto.UpdateCommissionRequest;
import lead.exchange.entity.Estate;
import lead.exchange.exception.ResourceNotFoundException;
import lead.exchange.model.EstateStatus;
import lead.exchange.repository.EstateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstateService {
    private final EstateRepository estateRepository;
    private final UserService userService;

    private Clock clock;

    public List<Estate> getEstateByUserId(UUID userId) {
        userService.checkUserExistByUserId(userId);
        return estateRepository.findByUserId(userId);
    }

    public Estate findById(UUID estateId) {
        return estateRepository.findById(estateId).orElseThrow(
                () -> new ResourceNotFoundException("Estate not found with id: " + estateId)
        );
    }

    public Estate archiveEstate(UUID estateId) {
        Estate estate = findById(estateId);
        estate.setStatus(EstateStatus.ARCHIVE);
        estate.setUpdatedAt(LocalDateTime.now(clock));
        return estateRepository.save(estate);
    }

    public Estate updateCommission(UUID estateId, UpdateCommissionRequest request) {
        Estate estate = findById(estateId);

        if (request.getTotalCommissionRate() != null) {
            estate.setTotalCommissionRate(request.getTotalCommissionRate());
        }

        if (request.getCommissionShare() != null) {
            estate.setCommissionShare(request.getCommissionShare());
        }

        estate.setUpdatedAt(LocalDateTime.now(clock));
        return estateRepository.save(estate);
    }

}
