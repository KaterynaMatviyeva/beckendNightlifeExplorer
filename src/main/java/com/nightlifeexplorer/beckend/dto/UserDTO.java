package com.nightlifeexplorer.beckend.dto;

import com.nightlifeexplorer.beckend.enums.Role;

public record UserDTO(
        Long id,
        String email,
        String username,
        Role role
) {}
