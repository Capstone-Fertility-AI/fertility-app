package com.capstone.fertility.domain.user.service.command;

import com.capstone.fertility.domain.user.converter.UserConverter;
import com.capstone.fertility.domain.user.dto.req.UserReqDTO;
import com.capstone.fertility.domain.user.dto.res.UserResDTO;
import com.capstone.fertility.domain.user.entity.User;
import com.capstone.fertility.domain.user.enums.Role;
import com.capstone.fertility.domain.user.exception.UserException;
import com.capstone.fertility.domain.user.exception.code.UserErrorCode;
import com.capstone.fertility.domain.user.repository.UserRepository;
import com.capstone.fertility.global.auth.service.RefreshTokenProvider;
import com.capstone.fertility.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;

    @Override
    public UserResDTO.LoginResDTO signUp(UserReqDTO.SignUpReqDTO request) {
        if (userRepository.existsByEmail(request.email())){
            throw new UserException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        User newUser = UserConverter.toLocalUser(request.email(), encodedPassword, request.nickname());
        User savedUser = userRepository.save(newUser);

        String accessToken = jwtTokenProvider.createToken(savedUser.getId(), Role.USER);
        String refreshToken = refreshTokenProvider.createAndSave(savedUser.getId());

        return UserConverter.toLoginResDTO(accessToken, refreshToken, savedUser);
    }
}
