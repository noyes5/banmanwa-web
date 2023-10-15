package com.banmanwa.web.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class MemberController {

    @GetMapping
    public ResponseEntity<Void> findUserInfo() {
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateMember() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/address")
    public ResponseEntity<Void> findMyAddress() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/address")
    public ResponseEntity<Void> addMyAddress() {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/address")
    public ResponseEntity<Void> updateMyAddress() {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/address")
    public ResponseEntity<Void> deleteMyAddress() {
        return ResponseEntity.noContent().build();
    }
}
