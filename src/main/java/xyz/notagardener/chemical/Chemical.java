package xyz.notagardener.chemical;

import xyz.notagardener.gardener.Gardener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "chemical")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"chemicalId", "name", "period"})
public class Chemical {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chemicalId;

    // 약품 이름
    @NotNull
    private String name;

    // 약품 종류: ex) 기본 NPK 비료, 미량 원소 비료, 살충제...
    @NotNull
    private String type;

    // 시비/살포 주기
    @NotNull
    private int period;

    @NotNull
    private String active;

    // 외래키가 있는 곳이 연관관계의 주인
    // 양방향 매핑 시 반대편에 mappedBy
    // 그러나 Gardener는 Chemical를 몰라도 상관없으므로 단방향 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gardener_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // ddl: on deleteOne cascade
    private Gardener gardener;

    @Builder
    public Chemical(Long chemicalId, String name, String type, int period, String active, Gardener gardener) {
        this.chemicalId = chemicalId;
        this.name = name;
        this.type = type;
        this.period = period;
        this.active = active;
        this.gardener = gardener;
    }

    public void deactivate() {
        this.active = "N";
    }

    public void update(String name, String type, int period) {
        this.name = name;
        this.type = type;
        this.period = period;
    }
}
