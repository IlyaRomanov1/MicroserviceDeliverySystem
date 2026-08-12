package com.ilyaromanov.authservice.services;

import com.ilyaromanov.authservice.models.dtos.UserDTO;
import com.ilyaromanov.authservice.models.dtos.requests.AuthRequestDTO;
import com.ilyaromanov.authservice.models.dtos.responses.AuthResponseDTO;

public interface AuthService {

    /**
     * Получает UserDTO маппит в User, шифрует пароль и сохраняет в БД
     */
    void register(UserDTO dto);

    /**
     * Получает AuthRequestDTO проверяет есть ли такой пользователь в бд возвращает AuthResponseDTO
     */
    AuthResponseDTO login(AuthRequestDTO dto);
}
