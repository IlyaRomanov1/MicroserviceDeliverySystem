package com.ilyaromanov.authservice.models.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDTO {

    private String email;
    private String password;
}
