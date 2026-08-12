package com.ilyaromanov.authservice.services.impl;

import com.ilyaromanov.authservice.models.dtos.UserDTO;
import com.ilyaromanov.authservice.models.dtos.requests.AuthRequestDTO;
import com.ilyaromanov.authservice.models.dtos.responses.AuthResponseDTO;
import com.ilyaromanov.authservice.models.entities.User;
import com.ilyaromanov.authservice.models.enums.Role;
import com.ilyaromanov.authservice.repositories.UserRepository;
import com.ilyaromanov.authservice.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(UserDTO dto) {
        User user = modelMapper.map(dto, User.class);
        user.setRole(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public AuthResponseDTO login(AuthRequestDTO dto) {
        User user = userRepository.getUserByEmail(dto.getEmail());
        if (passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            return modelMapper.map(user, AuthResponseDTO.class);
        return null;
    }
}
