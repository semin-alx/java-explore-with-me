package ru.practicum.compilation.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compilations")
@Builder
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pinned")
    private Boolean pinned;

    @Column(name = "title", nullable = false)
    private String title;

    @OneToMany
    @JoinColumn(name = "comp_id", insertable = false, updatable = false)
    private List<CompilationEvents> compilationEvents;

}
