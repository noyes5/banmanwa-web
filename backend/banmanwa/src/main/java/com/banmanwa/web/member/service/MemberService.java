package com.banmanwa.web.member.service;

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

    public void add(Member member) {
        memberRepository.save(member);
    }

    public Optional<Member> find(long id) {
        return memberRepository.findById(id);
    }
}


