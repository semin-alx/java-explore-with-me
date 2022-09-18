package ru.practicum.compilation.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.common.error_handling.exception.ObjectNotFoundException;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.helper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.CompilationEvents;
import ru.practicum.compilation.repository.CompilationEventsRepository;
import ru.practicum.compilation.repository.CompilationRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationEventsRepository compilationEventsRepository;

    public CompilationServiceImpl(CompilationRepository compilationRepository,
                                  CompilationEventsRepository compilationEventsRepository) {
        this.compilationRepository = compilationRepository;
        this.compilationEventsRepository = compilationEventsRepository;
    }

    private Compilation getAndCheck(Long compId) {

        Optional<Compilation> compilation = compilationRepository.findById(compId);

        if (!compilation.isPresent()) {
            throw new ObjectNotFoundException("Указанная подборка не найдена");
        }

        return compilation.get();

    }

    @Override
    @Transactional
    public CompilationDto create(NewCompilationDto newCompilationDto) {

        Compilation compNew = CompilationMapper.toCompilation(newCompilationDto);
        final Compilation compSaved = compilationRepository.save(compNew);

        List<CompilationEvents> compilationEvents = newCompilationDto.getEvents().stream()
                .map(eventId -> new CompilationEvents(null, compSaved.getId(), eventId))
                .collect(Collectors.toList());

        compilationEvents.stream()
                .forEach(o -> compilationEventsRepository.save(o));

        compSaved.setCompilationEvents(compilationEvents);

        return CompilationMapper.toCompilationDto(compSaved);

    }

    @Override
    public CompilationDto findById(long compId) {
        Compilation compilation = getAndCheck(compId);
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    public void deleteById(long compId) {
        getAndCheck(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public void deleteEvent(long compId, long eventId) {

        Optional<CompilationEvents> ce =
                compilationEventsRepository.findByCompIdAndEventId(compId, eventId);

        if (!ce.isPresent()) {
            throw new ObjectNotFoundException("Указанное событие в поборке не найдено");
        }

        compilationEventsRepository.deleteById(ce.get().getId());

    }

    @Override
    public void addEvent(long compId, long eventId) {
        CompilationEvents ce = new CompilationEvents(null, compId, eventId);
        compilationEventsRepository.save(ce);
    }

    @Override
    public void disablePin(long compId) {
        Compilation compilation = getAndCheck(compId);
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public void enablePin(long compId) {
        Compilation compilation = getAndCheck(compId);
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    @Override
    public List<CompilationDto> find(Boolean pinned, Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size);

        if (pinned == null) {
            return compilationRepository.findAll(pageable).stream()
                    .map(CompilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
        } else {
            return compilationRepository.findByPinned(pinned, pageable).stream()
                    .map(CompilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
        }

    }
}
