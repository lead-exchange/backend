package lead.exchange.dto;

import lombok.Data;

@Data
public class UpdateCommissionRequest {
    private Double totalCommissionRate;
    private Double commissionShare;
}

