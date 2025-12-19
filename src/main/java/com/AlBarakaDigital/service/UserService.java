package com.AlBarakaDigital.service;

import com.AlBarakaDigital.dto.UserRequestDTO;
import com.AlBarakaDigital.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO userRequestDTO);

    UserResponseDTO getUserById(Long id);

    UserResponseDTO getUserByEmail(String email);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO activateUser(Long id);

    UserResponseDTO deactivateUser(Long id);

    void deleteUser(Long id);
}
