package com.banmanwa.web.member.domain;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Member extends Person {

    @OneToMany
    private List<Member> friends;

    public Member() {
    }

    public Member(String id, String name) {
        super(id, name);
    }
}