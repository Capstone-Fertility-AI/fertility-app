package com.capstone.fertility.domain.user.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capstone.fertility.domain.user.converter.UserConverter;
import com.capstone.fertility.domain.user.dto.req.UserReqDTO;
import com.capstone.fertility.domain.user.dto.res.UserResDTO;
import com.capstone.fertility.domain.user.entity.User;
import com.capstone.fertility.domain.user.repository.UserRepository;

import com.capstone.fertility.domain.user.exception.UserException;
import com.capstone.fertility.domain.user.exception.code.UserErrorCode;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandServiceImpl implements UserCommandService {

    // 1. UserCommandService 인터페이스를 구현(implements)해야 합니다.
    private final UserRepository userRepository;

    // 2. 인터페이스에 정의된 메서드를 @Override 하여 구현해야 합니다.
    //    이것이 바로 코드가 들어갈 "방" 입니다.
    @Override
    public UserResDTO.UserInfoDTO updateMyInfo(Long userId, UserReqDTO.UpdateProfileDTO request) {
        // 3. 메서드 안에서 로직을 수행합니다.

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_ID_NOT_FOUND));

        user.updateProfile(request.getNickname(), request.getProfileImageUrl());

        // 4. 메서드의 가장 마지막에 return 문으로 결과를 반환합니다.
        //    이때 사용되는 'user' 변수는 바로 위에서 조회한 결과물입니다.
        return UserConverter.toUserInfoDTO(user);
    }
}
