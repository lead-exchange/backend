package lead.exchange.dto;

import lead.exchange.model.Requirements;

public record LeadCreateDto(
    String name,
    Requirements requirements,
    Double commissionShare
) {

}
