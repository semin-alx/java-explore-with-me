package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(UserDto userDto);

    List<UserDto> find(Long[] ids, Integer from, Integer size);

    void deleteById(long id);

}
