package lead.exchange.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lead.exchange.model.Requirements;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("leads")
public class Lead {
    @Id
    @Column("id")
    private UUID id;
    @Column("user_id")
    private UUID userId;
    private Requirements requirements;
    private String status;
    @Column("commission_share")
    private Double commissionShare;
    @Column("created_at")
    private LocalDateTime createdAt;
    @Column("updated_at")
    private LocalDateTime updatedAt;

    public Lead() {
    }

    public Lead(UUID id, UUID userId, Requirements requirements, String status,
                Double commissionShare, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.requirements = requirements;
        this.status = status;
        this.commissionShare = commissionShare;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Requirements getRequirements() {
        return requirements;
    }

    public void setRequirements(Requirements requirements) {
        this.requirements = requirements;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getCommissionShare() {
        return commissionShare;
    }

    public void setCommissionShare(Double commissionShare) {
        this.commissionShare = commissionShare;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lead lead = (Lead) o;
        return Objects.equals(id, lead.id)
                && Objects.equals(userId, lead.userId)
                && Objects.equals(requirements, lead.requirements)
                && Objects.equals(status, lead.status)
                && Objects.equals(commissionShare, lead.commissionShare)
                && Objects.equals(createdAt, lead.createdAt)
                && Objects.equals(updatedAt, lead.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, requirements, status, commissionShare, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Lead{"
                + "id=" + id
                + ", userId=" + userId
                + ", requirements=" + requirements
                + ", status='" + status + '\''
                + ", commissionShare=" + commissionShare
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + '}';
    }
}
