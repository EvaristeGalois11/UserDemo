package it.nave.userdemo.controller;

import it.nave.userdemo.dto.UserDto;
import it.nave.userdemo.dto.UserFilter;
import it.nave.userdemo.service.UserService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public long create(@RequestBody UserDto dto) {
        return userService.create(dto);
    }

    @GetMapping(path = "{id}")
    public UserDto read(@PathVariable long id) {
        return userService.read(id);
    }

    @PutMapping(path = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable long id, @RequestBody UserDto dto) {
        dto.setId(id);
        userService.update(dto);
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        userService.delete(id);
    }

    @GetMapping
    public Page<UserDto> filter(@ParameterObject UserFilter filter, @ParameterObject Pageable pageable) {
        return userService.filter(filter, pageable);
    }
}
