package lead.exchange.dto;

import lead.exchange.model.LeadStatus;
import lead.exchange.model.Requirements;

public record LeadUpdateDto(

    String name,
    Requirements requirements,
    LeadStatus status,
    Double commissionShare
) {

}
