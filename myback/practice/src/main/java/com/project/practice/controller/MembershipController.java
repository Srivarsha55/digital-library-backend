package com.project.practice.controller;

import com.project.practice.model.Membership;
import com.project.practice.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/memberships")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    @PutMapping("/upgrade/{loginId}")
    public ResponseEntity<Membership> upgradeToPremium(@PathVariable Long loginId, @RequestParam int amount) {
        Membership upgradedMembership = membershipService.upgradeToPremium(loginId, amount);
        if (upgradedMembership != null) {
            return ResponseEntity.ok(upgradedMembership);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
