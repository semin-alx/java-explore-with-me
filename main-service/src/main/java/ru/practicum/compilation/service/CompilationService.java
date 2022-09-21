package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {

    CompilationDto create(NewCompilationDto newCompilationDto);

    CompilationDto findById(long compilationId);

    void deleteById(long compilationId);

    void deleteEvent(long compilationId, long eventId);

    void addEvent(long compilationId, long eventId);

    void disablePin(long compilationId);

    void enablePin(long compilationId);

    List<CompilationDto> find(Boolean pinned, Integer from, Integer size);

}
