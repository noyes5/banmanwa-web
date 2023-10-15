package com.banmanwa.web.member.service;

import com.banmanwa.web.auth.dto.ProfileDto;
import com.banmanwa.web.member.dao.MemberRepository;
import com.banmanwa.web.member.domain.Member;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member add(ProfileDto profile) {
        if (find(profile.getId()).isEmpty()) {
            return memberRepository.save(new Member(profile.getId(), profile.getNickname()));
        }
        return null;
    }

    public Optional<Member> find(String id) {
        return memberRepository.findById(id);
    }
}


