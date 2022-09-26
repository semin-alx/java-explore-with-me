package ru.practicum.user.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.service.UserService;
import ru.practicum.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET BaseUrl/admin/users?ids=1&ids=2&from=2&size=15
    @GetMapping
    public List<UserDto> find(@RequestParam(required = false) Long[] ids,
                              @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                              @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        return userService.find(ids, from, size);
    }

    // POST BaseUrl/admin/users
    // Body: Json UserDto
    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    // DELETE BaseUrl/admin/users/{userId}
    @DeleteMapping(value = "/{userId}")
    public void delete(@PathVariable @Positive long userId) {
        userService.deleteById(userId);
    }

}
