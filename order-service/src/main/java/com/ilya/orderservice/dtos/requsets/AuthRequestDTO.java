package com.ilya.orderservice.dtos.requsets;

import com.ilya.orderservice.models.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDTO {

    private Long id;
    private Role role;
}
