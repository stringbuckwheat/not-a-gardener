package xyz.notagardener.status.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import xyz.notagardener.common.validation.YesOrNoType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "status_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@ToString(of = {"statusLogId", "status", "active", "recordedDate", "createDate"})
public class StatusLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statusLogId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusType statusType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private YesOrNoType active;

    @NotNull
    @Column(name = "recorded_date")
    private LocalDate recordedDate;

    @ManyToOne
    @JoinColumn(name = "status_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Status status;

    @CreatedDate
    private LocalDateTime createDate;

    public Long getGardenerId() {
        return status.getPlant().getGardener().getGardenerId();
    }

    public void update(StatusType statusType, LocalDate recordedDate, Status status) {
        this.statusType = statusType;
        this.recordedDate = recordedDate;
        this.status = status;
    }

    @Builder
    public StatusLog(Long statusLogId, StatusType statusType, YesOrNoType active, LocalDate recordedDate, Status status, LocalDateTime createDate) {
        this.statusLogId = statusLogId;
        this.statusType = statusType;
        this.active = active;
        this.recordedDate = recordedDate;
        this.status = status;
        this.createDate = createDate;
    }
}