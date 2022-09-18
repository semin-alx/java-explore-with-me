package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {

    CompilationDto create(NewCompilationDto newCompilationDto);

    CompilationDto findById(long compId);

    void deleteById(long compId);

    void deleteEvent(long compId, long eventId);

    void addEvent(long compId, long eventId);

    void disablePin(long compId);

    void enablePin(long compId);

    List<CompilationDto> find(Boolean pinned, Integer from, Integer size);

}
