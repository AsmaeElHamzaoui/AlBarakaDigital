package com.AlBarakaDigital.dto;

import com.AlBarakaDigital.enums.Role;
import lombok.Data;

@Data
public class UserRequestDTO {
    private String email;
    private String password;
    private String fullName;
    private Role role;
}

