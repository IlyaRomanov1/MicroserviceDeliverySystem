package com.ilyaromanov.authservice.models.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private String email;
    private String name;
    private Byte age;
    private String password;
}


