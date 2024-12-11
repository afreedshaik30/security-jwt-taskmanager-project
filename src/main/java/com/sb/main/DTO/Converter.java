package com.sb.main.DTO;

import com.sb.main.entities.Users;

public class Converter
{

    public static Users userDtoToEntity(UserDto userDto)
    {
        Users user = new Users();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return user;
    }

    public static UserDto entityToUserDto(Users user)
    {
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        return userDto;
    }
}
