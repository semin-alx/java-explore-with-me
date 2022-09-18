package ru.practicum.compilation.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/admin/compilations")
@Validated
public class AdminCompilationController {

    private final CompilationService compilationService;

    public AdminCompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    public CompilationDto create(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return compilationService.create(newCompilationDto);
    }

    @DeleteMapping(value = "/{compId}")
    public void delete(@PathVariable @Positive long compId) {
        compilationService.deleteById(compId);
    }

    @DeleteMapping(value = "/{compId}/events/{eventId}")
    public void deleteEvent(@PathVariable @Positive long compId,
                            @PathVariable @Positive long eventId) {
        compilationService.deleteEvent(compId, eventId);
    }

    @PatchMapping(value = "/{compId}/events/{eventId}")
    public void addEvent(@PathVariable @Positive long compId,
                            @PathVariable @Positive long eventId) {
        compilationService.addEvent(compId, eventId);
    }

    @DeleteMapping(value = "/{compId}/pin")
    public void disablePin(@PathVariable @Positive long compId) {
        compilationService.disablePin(compId);
    }

    @PatchMapping(value = "/{compId}/pin")
    public void enablePin(@PathVariable @Positive long compId) {
        compilationService.enablePin(compId);
    }

}
