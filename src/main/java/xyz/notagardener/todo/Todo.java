package xyz.notagardener.domain.todo;

import xyz.notagardener.domain.gardener.Gardener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name="todo")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId;

    @NotNull
    private String task;

    private LocalDate deadline;
    private LocalDate createDate;
    private LocalDate completeDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Gardener gardener;

    public void update(String task, LocalDate deadline){
        this.task = task;
        this.deadline = deadline;
    }

    public void complete(LocalDate completeDate){
        this.completeDate = completeDate;
    }
}
