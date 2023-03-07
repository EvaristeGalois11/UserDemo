package it.nave.userdemo.service;

import it.nave.userdemo.dto.UserDto;
import it.nave.userdemo.mapper.UserMapper;
import it.nave.userdemo.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDto get(long id) {
        return userRepository.findById(id).map(userMapper::toDto).orElseThrow();
    }

    public long create(UserDto dto) {
        return userRepository.save(userMapper.toEntity(dto)).getId();
    }

    public void update(UserDto dto) {
        userRepository.save(userMapper.toEntity(dto));
    }

    public void delete(long id) {
        userRepository.deleteById(id);
    }
}
