package lead.exchange.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("matches")
public class Match {
    @Id
    @Column("id")
    private UUID id;
    @Column("lead_id")
    private UUID leadId;
    @Column("estate_id")
    private UUID estateId;
    private String status;
    @Column("matched_at")
    private LocalDateTime matchedAt;

    public Match() {
    }

    public Match(UUID id, UUID leadId, UUID estateId, String status, LocalDateTime matchedAt) {
        this.id = id;
        this.leadId = leadId;
        this.estateId = estateId;
        this.status = status;
        this.matchedAt = matchedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getLeadId() {
        return leadId;
    }

    public void setLeadId(UUID leadId) {
        this.leadId = leadId;
    }

    public UUID getEstateId() {
        return estateId;
    }

    public void setEstateId(UUID estateId) {
        this.estateId = estateId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getMatchedAt() {
        return matchedAt;
    }

    public void setMatchedAt(LocalDateTime matchedAt) {
        this.matchedAt = matchedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Match match = (Match) o;
        return Objects.equals(id, match.id)
                && Objects.equals(leadId, match.leadId)
                && Objects.equals(estateId, match.estateId)
                && Objects.equals(status, match.status)
                && Objects.equals(matchedAt, match.matchedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, leadId, estateId, status, matchedAt);
    }

    @Override
    public String toString() {
        return "Match{"
                + "id=" + id
                + ", leadId=" + leadId
                + ", estateId=" + estateId
                + ", status='" + status + '\''
                + ", matchedAt=" + matchedAt
                + '}';
    }
}
