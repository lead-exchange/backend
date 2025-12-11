package lead.exchange.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lead.exchange.dto.CreateMatchDto;
import lead.exchange.dto.ResponseMatchWithEstateDto;
import lead.exchange.dto.ResponseMatchWithLeadDto;
import lead.exchange.dto.UpdateMatchDto;
import lead.exchange.dto.UserType;
import lead.exchange.entity.Estate;
import lead.exchange.entity.Lead;
import lead.exchange.entity.Match;
import lead.exchange.entity.MatchLog;
import lead.exchange.entity.MatchUpdateEntity;
import lead.exchange.entity.User;
import lead.exchange.exception.ForbiddenException;
import lead.exchange.exception.ResourceAlreadyExistsException;
import lead.exchange.exception.ResourceNotFoundException;
import lead.exchange.mapper.MatchMapper;
import lead.exchange.model.MatchCommonStatus;
import lead.exchange.model.MatchStatus;
import lead.exchange.repository.EstateRepository;
import lead.exchange.repository.LeadRepository;
import lead.exchange.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {

    public static final String MATCH_WITH_THIS_ID_S_NOT_FOUND = "Match with this id %s not found";
    private final MatchRepository matchRepository;
    private final MatchLogService matchLogService;
    private final LeadRepository leadRepository;
    private final EstateRepository estateRepository;
    private final MatchMapper mapper;
    private final UserService userService;
    private final TelegramNotificationService telegramNotificationService;
    private final ContactSharingService contactSharingService;

    public List<ResponseMatchWithEstateDto> getMatchesByLeadId(UUID leadId) {
        log.debug("Fetching matches by lead id: {}", leadId);
        return matchRepository.findByLeadId(leadId).stream().map(mapper::toDto).toList();
    }

    public Match getMatchById(UUID matchId) {
        log.debug("Fetching match by id: {}", matchId);
        return matchRepository.findById(matchId)
            .orElseThrow(() -> new IllegalArgumentException("Match with this id not found"));
    }

    public List<ResponseMatchWithLeadDto> getMatchesByEstateId(UUID estateId) {
        log.debug("Fetching matches by estate id: {}", estateId);
        return matchRepository.findByEstateId(estateId)
            .stream()
            .map(mapper::toDto)
            .map(e -> e.withLeadName("Имя скрыто"))
            .toList();
    }

    @Transactional
    public Match createMatch(CreateMatchDto dto, UUID id) {
        log.info("Creating new match for lead: {} and estate: {}", dto.leadId(), dto.estateId());
        matchRepository.findByEstateIdAndLeadId(dto.estateId(), dto.leadId()).ifPresent(
            e -> {
                throw new ResourceAlreadyExistsException("The match with this lead id and estate id already exists");
            }
        );

        Optional<Lead> lead = leadRepository.findById(dto.leadId());
        Optional<Estate> estate = estateRepository.findById(dto.estateId());

        if (lead.isPresent() && estate.isPresent() && lead.get().getUserId().equals(estate.get().getUserId())) {
            throw new ForbiddenException("Unable to create match with itself");
        }

        Match match = mapper.toEntity(dto, id);

        UserType userType = getUserType(dto.leadId(), dto.estateId(), id);

        switch (userType) {
            case LEAD -> {
                match.setLeadStatus(dto.status());
                validateStatusTransition(match.getEstateStatus(), match.getLeadStatus());
                MatchCommonStatus commonStatus = getCommonStatus(
                    match.getEstateStatus(),
                    match.getLeadStatus(),
                    MatchCommonStatus.WAIT_ESTATE
                );
                match.setCommonStatus(commonStatus);
            }
            case ESTATE -> {
                match.setEstateStatus(dto.status());
                validateStatusTransition(match.getLeadStatus(), match.getEstateStatus());
                MatchCommonStatus commonStatus = getCommonStatus(
                    match.getLeadStatus(),
                    match.getEstateStatus(),
                    MatchCommonStatus.WAIT_LEAD
                );
                match.setCommonStatus(commonStatus);
            }
            default -> throw new RuntimeException("Wrong user type when create match");
        }


        Match savedMatch = matchRepository.save(match);
        matchLogService.createMatchLog(buildMatchLog(savedMatch, dto.status(), userType));

        return savedMatch;
    }

    @Transactional
    public Match updateMatch(UpdateMatchDto dto, UUID username) {
        Match createdMatch = matchRepository.findById(dto.id())
            .orElseThrow(() -> new ResourceNotFoundException(MATCH_WITH_THIS_ID_S_NOT_FOUND.formatted(dto.id())));

        UserType userType = getUserType(createdMatch.getLeadId(), createdMatch.getEstateId(), username);

        switch (userType) {
            case LEAD -> {
                validateStatusTransition(createdMatch.getEstateStatus(), dto.status());
                MatchCommonStatus commonStatus = getCommonStatus(
                    createdMatch.getEstateStatus(),
                    dto.status(),
                    MatchCommonStatus.WAIT_ESTATE
                );
                MatchUpdateEntity toSave = mapper.toEntity(dto, username, commonStatus);
                matchRepository.updateLeadMatch(toSave);
            }
            case ESTATE -> {
                validateStatusTransition(createdMatch.getLeadStatus(), dto.status());
                MatchCommonStatus commonStatus = getCommonStatus(
                    createdMatch.getLeadStatus(),
                    dto.status(),
                    MatchCommonStatus.WAIT_LEAD
                );
                MatchUpdateEntity toSave = mapper.toEntity(dto, username, commonStatus);
                matchRepository.updateEstateMatch(toSave);
            }
            default -> throw new RuntimeException("Wrong user type when update match");
        }


        Match savedMatch = matchRepository.findById(dto.id())
            .orElseThrow(() -> new RuntimeException(MATCH_WITH_THIS_ID_S_NOT_FOUND.formatted(dto.id())));

        matchLogService.createMatchLog(buildMatchLog(savedMatch, dto.status(), userType));

        if (isSuccess(savedMatch.getLeadStatus(), savedMatch.getEstateStatus())) {

            Lead lead = leadRepository.findById(savedMatch.getLeadId()).orElseThrow();
            Estate estate = estateRepository.findById(savedMatch.getEstateId()).orElseThrow();
            User userLead = userService.getUserById(lead.getUserId());
            User userEstate = userService.getUserById(estate.getUserId());

            telegramNotificationService.sendNotification(
                    String.format(
                            "🎉 Поздравляем! У вас случился мэтч для лида \"%s\" с объектом \"%s\".",
                            lead.getName(),
                            estate.getAttributes().getTitle()
                    ),
                    userLead.getTelegramId()
            );

            contactSharingService.sendTelegramContact(
                    userLead.getTelegramId(),
                    userEstate
            );

            telegramNotificationService.sendNotification(
                    String.format(
                            "🎉 Поздравляем! У вас случился мэтч для объекта \"%s\" с лидом \"%s\".",
                            estate.getAttributes().getTitle(),
                            lead.getName()
                    ),
                    userEstate.getTelegramId()
            );

            contactSharingService.sendTelegramContact(
                    userEstate.getTelegramId(),
                    userLead
            );
        }

        return savedMatch;
    }

    private void validateStatusTransition(MatchStatus collegeStatus, MatchStatus newStatus) {
        boolean isValidTransition = switch (collegeStatus) {
            case UNDEFINED -> isAllowedUndefinedTransition(newStatus);
            case COMMISSION -> isAllowedCommissionTransition(newStatus);
            case LIKED -> isAllowedLikedTransition(newStatus);
            default -> false;
        };

        if (!isValidTransition) {
            throw new IllegalArgumentException(
                "Invalid status transition" + newStatus);
        }
    }

    private MatchCommonStatus getCommonStatus(
        MatchStatus collegeStatus,
        MatchStatus newStatus,
        MatchCommonStatus waitStatus
    ) {
        return switch (collegeStatus) {
            case UNDEFINED -> getCommonUndefined(newStatus, waitStatus);
            case COMMISSION -> getCommonCommission(newStatus, waitStatus);
            case LIKED -> getCommonLiked(newStatus, waitStatus);
            default -> throw new IllegalArgumentException();
        };
    }

    private UserType getUserType(UUID leadId, UUID estateId, UUID updatedBy) {
        UserType userType;
        if (leadRepository.findById(leadId)
            .orElseThrow(() -> new ResourceNotFoundException("Lead with this id %s not found".formatted(leadId)))
            .getUserId()
            .equals(updatedBy)) {

            userType = UserType.LEAD;
        } else if (estateRepository.findById(estateId)
            .orElseThrow(() -> new ResourceNotFoundException("Estate with this id %s not found".formatted(estateId)))
            .getUserId()
            .equals(updatedBy)) {

            userType = UserType.ESTATE;
        } else {
            throw new ForbiddenException("You are not allowed to create or update match");
        }
        return userType;
    }

    private MatchCommonStatus getCommonUndefined(MatchStatus newStatus, MatchCommonStatus waitStatus) {
        if (newStatus == MatchStatus.LIKED) {
            return waitStatus;
        }
        if (newStatus == MatchStatus.COMMISSION) {
            return waitStatus;
        }
        if (newStatus == MatchStatus.DISLIKED) {
            return MatchCommonStatus.FAILED;
        }
        throw new IllegalArgumentException("Get common undefined status failed");
    }

    private MatchCommonStatus getCommonCommission(MatchStatus newStatus, MatchCommonStatus waitStatus) {
        if (newStatus == MatchStatus.COMMISSION) {
            return waitStatus;
        }
        if (newStatus == MatchStatus.ACCEPTED) {
            return MatchCommonStatus.SUCCESS;
        }
        if (newStatus == MatchStatus.DECLINED) {
            return MatchCommonStatus.FAILED;
        }
        throw new IllegalArgumentException("Get common commission status failed");
    }

    private MatchCommonStatus getCommonLiked(MatchStatus newStatus, MatchCommonStatus waitStatus) {
        if (newStatus == MatchStatus.COMMISSION) {
            return waitStatus;
        }
        if (newStatus == MatchStatus.LIKED) {
            return MatchCommonStatus.SUCCESS;
        }
        if (newStatus == MatchStatus.DISLIKED) {
            return MatchCommonStatus.FAILED;
        }
        throw new IllegalArgumentException("Get common liked status failed");
    }


    private boolean isAllowedUndefinedTransition(MatchStatus newStatus) {
        return newStatus == MatchStatus.LIKED
            || newStatus == MatchStatus.DISLIKED
            || newStatus == MatchStatus.COMMISSION;
    }

    private boolean isAllowedCommissionTransition(MatchStatus newStatus) {
        return newStatus == MatchStatus.COMMISSION
            || newStatus == MatchStatus.ACCEPTED
            || newStatus == MatchStatus.DECLINED;
    }

    private boolean isAllowedLikedTransition(MatchStatus newStatus) {
        return newStatus == MatchStatus.LIKED
            || newStatus == MatchStatus.DISLIKED
            || newStatus == MatchStatus.COMMISSION;
    }


    private MatchLog buildMatchLog(Match match, MatchStatus status, UserType userType) {
        return MatchLog.builder()
            .matchId(match.getId())
            .status(status)
            .leadCommission(match.getLeadCommission())
            .updatedBy(match.getUpdatedBy())
            .comment(match.getComment())
            .createdAt(match.getUpdatedAt())
            .userType(userType.name())
            .build();
    }

    private boolean isSuccess(MatchStatus leadStatus, MatchStatus estateStatus) {
        if (leadStatus.equals(MatchStatus.LIKED) && estateStatus.equals(MatchStatus.LIKED)) {
            return true;
        }
        if (leadStatus.equals(MatchStatus.COMMISSION) && estateStatus.equals(MatchStatus.ACCEPTED)) {
            return true;
        }
        if (leadStatus.equals(MatchStatus.ACCEPTED) && estateStatus.equals(MatchStatus.COMMISSION)) {
            return true;
        }
        return false;
    }
}
