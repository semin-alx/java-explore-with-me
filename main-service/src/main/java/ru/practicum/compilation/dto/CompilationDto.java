package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class CompilationDto {

    private Long id;
    private Set<Long> events;
    private Boolean pinned;
    private String title;

}
