package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.common.error_handling.exception.ObjectNotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.helper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {

        if ((userDto.getId() != null) && (userDto.getId() != 0)) {
            throw new IllegalArgumentException("При создании пользователя id должен быть 0");
        }

        User user = UserMapper.toUser(userDto);
        user = userRepository.save(user);
        return UserMapper.toUserDto(user);

    }

    @Override
    public List<UserDto> find(Long[] ids, Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size);

        if (ids == null) {
            return userRepository.findAll(pageable).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findByIds(ids, pageable).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }

    }

    @Override
    public void deleteById(long id) {

        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new ObjectNotFoundException("Пользователь с таким id в базе не найден");
        }

    }

}
