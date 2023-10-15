package com.banmanwa.web.member.domain;

import com.banmanwa.web.auth.dto.ProfileDto;
import com.banmanwa.web.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("멤버 관리 테스트")
@SpringBootTest
public class MemberTest {

    @Autowired
    private MemberService service;

    @DisplayName("첫 로그인시 멤버 저장")
    @Test
    @Transactional
    public void firstLoginSave() {
        ProfileDto profile = new ProfileDto("12345", "임상현", "imageLink");
        Member member = service.add(profile);
        assertThat(service.find("12345").get()).isEqualTo(member);
    }

    @DisplayName("첫 로그인이 아니면 저장하지 않음")
    @Test
    public void secondLoginNotSave() {
        ProfileDto profile = new ProfileDto("12345", "임상현", "imageLink");
        service.add(profile);
        Member member = service.add(profile);
        assertThat(member).isNull();
    }
}
