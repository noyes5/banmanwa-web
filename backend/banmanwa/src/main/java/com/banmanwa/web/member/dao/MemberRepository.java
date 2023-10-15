package com.banmanwa.web.member.dao;

import com.banmanwa.web.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

}
