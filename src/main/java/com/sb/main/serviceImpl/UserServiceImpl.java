package com.sb.main.serviceImpl;

import com.sb.main.DTO.Converter;
import com.sb.main.DTO.UserDto;
import com.sb.main.entities.Users;
import com.sb.main.repository.UserRepository;
import com.sb.main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto userDto)
    {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword())); // encode password with Password Encoder Bean

        Users user = Converter.userDtoToEntity(userDto); // dto to entity

        Users savedUser = userRepository.save(user); // save user

        UserDto dto = Converter.entityToUserDto(savedUser); // entity to dto

        return dto;
    }
}
