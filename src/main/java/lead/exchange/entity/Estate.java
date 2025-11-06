package lead.exchange.entity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;
import lead.exchange.model.EstateAttributes;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("estates")
public class Estate {
    @Id
    @Column("id")
    private UUID id;
    @Column("user_id")
    private UUID userId;
    private EstateAttributes attributes;
    @Column("total_commission_rate")
    private Double totalCommissionRate;
    @Column("commission_share")
    private Double commissionShare;
    private String status;
    @Column("created_at")
    private LocalDateTime createdAt;
    @Column("updated_at")
    private LocalDateTime updatedAt;

    public Estate() {
    }

    public Estate(UUID userId, EstateAttributes attributes, Double totalCommissionRate,
                  Double commissionShare, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.attributes = attributes;
        this.totalCommissionRate = totalCommissionRate;
        this.commissionShare = commissionShare;
        this.status = status;
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

    public EstateAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(EstateAttributes attributes) {
        this.attributes = attributes;
    }

    public Double getTotalCommissionRate() {
        return totalCommissionRate;
    }

    public void setTotalCommissionRate(Double totalCommissionRate) {
        this.totalCommissionRate = totalCommissionRate;
    }

    public Double getCommissionShare() {
        return commissionShare;
    }

    public void setCommissionShare(Double commissionShare) {
        this.commissionShare = commissionShare;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt.truncatedTo(ChronoUnit.MICROS);
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt.truncatedTo(ChronoUnit.MICROS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Estate estate = (Estate) o;
        return Objects.equals(id, estate.id)
                && Objects.equals(userId, estate.userId)
                && Objects.equals(attributes, estate.attributes)
                && Objects.equals(totalCommissionRate, estate.totalCommissionRate)
                && Objects.equals(commissionShare, estate.commissionShare)
                && Objects.equals(status, estate.status)
                && Objects.equals(createdAt, estate.createdAt)
                && Objects.equals(updatedAt, estate.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, attributes, totalCommissionRate, commissionShare, status, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Estate{"
                + "id=" + id
                + ", userId=" + userId
                + ", attributes=" + attributes
                + ", totalCommissionRate=" + totalCommissionRate
                + ", commissionShare=" + commissionShare
                + ", status='" + status + '\''
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + '}';
    }
}
