package com.banmanwa.web.auth.service;

import com.banmanwa.web.auth.dto.ProfileDto;
import com.banmanwa.web.auth.dto.ProfileTokenDto;
import com.banmanwa.web.auth.infrastructure.JwtTokenProvider;
import com.banmanwa.web.exception.AuthException;
import com.banmanwa.web.exception.BanManWaException;
import com.banmanwa.web.member.service.MemberService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    public ProfileTokenDto createToken(ProfileDto profile) {
        String token = jwtTokenProvider.createToken(profile.getId());
        return new ProfileTokenDto(profile, token);
    }

    public void validate(String token) {
        String id = jwtTokenProvider.extractId(token);
        if (memberService.find(id).isEmpty()) {
            throw new BanManWaException(AuthException.NOT_FOUND_USER);
        }
    }
}
