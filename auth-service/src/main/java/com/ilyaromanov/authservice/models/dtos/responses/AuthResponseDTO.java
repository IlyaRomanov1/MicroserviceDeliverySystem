package com.ilyaromanov.authservice.models.dtos.responses;

import com.ilyaromanov.authservice.models.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDTO {

    private Long id;
    private Role role;
}
