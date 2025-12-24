package com.ngovantai.example901.service;

import com.ngovantai.example901.dto.UserDto;
import com.ngovantai.example901.entity.User;

import java.util.List;

public interface UserService {
    User createUser(UserDto dto);

    List<User> getAllUsers();

    User getUserById(Long id);
}
