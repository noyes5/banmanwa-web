package com.banmanwa.web.group.domain;

import com.banmanwa.web.member.domain.Member;

import java.util.List;

public class Group {

    private Long id;
    private String name;
    private List<Member> members;
    private List<History> histories;
}
