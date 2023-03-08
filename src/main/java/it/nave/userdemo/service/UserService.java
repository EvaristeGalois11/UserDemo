/*
 * Copyright (C) 2023 Claudio Nave
 *
 * This file is part of UserDemo.
 *
 * UserDemo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UserDemo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.nave.userdemo.service;

import it.nave.userdemo.dto.UserDto;
import it.nave.userdemo.dto.UserFilter;
import it.nave.userdemo.entity.User;
import it.nave.userdemo.entity.User_;
import it.nave.userdemo.mapper.UserMapper;
import it.nave.userdemo.repository.UserRepository;
import jakarta.persistence.metamodel.SingularAttribute;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private static final String LIKE_FORMAT = "%%%s%%";

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public long create(UserDto dto) {
        return userRepository.save(userMapper.toEntity(dto)).getId();
    }

    public UserDto read(long id) {
        return userRepository.findById(id).map(userMapper::toDto).orElseThrow();
    }

    public void update(UserDto dto) {
        userRepository.save(userMapper.toEntity(dto));
    }

    public void delete(long id) {
        userRepository.deleteById(id);
    }

    public Page<UserDto> filter(UserFilter filter, Pageable pageable) {
        var userSpecification = buildSpecification(filter);
        return userRepository.findAll(userSpecification, pageable).map(userMapper::toDto);
    }

    private Specification<User> buildSpecification(UserFilter filter) {
        List<Specification<User>> specifications = new ArrayList<>();
        if (StringUtils.isNotBlank(filter.name())) {
            specifications.add(likeSpecification(User_.name, filter.name()));
        }
        if (StringUtils.isNotBlank(filter.surname())) {
            specifications.add(likeSpecification(User_.surname, filter.surname()));
        }
        if (StringUtils.isNotBlank(filter.mail())) {
            specifications.add(likeSpecification(User_.mail, filter.mail()));
        }
        if (StringUtils.isNotBlank(filter.address())) {
            specifications.add(likeSpecification(User_.address, filter.address()));
        }
        return specifications
                .stream()
                .reduce(Specification::or)
                .orElse(null);
    }

    private Specification<User> likeSpecification(SingularAttribute<User, String> attribute, String value) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get(attribute)), LIKE_FORMAT.formatted(value.toLowerCase()));
    }
}
