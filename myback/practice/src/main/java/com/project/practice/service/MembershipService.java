package com.project.practice.service;

import com.project.practice.model.Membership;
import com.project.practice.model.Login;
import com.project.practice.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class MembershipService {

    @Autowired
    private MembershipRepository membershipRepository;

    public Membership createDefaultMembership(Login login) {
        Membership membership = new Membership(login);
        return membershipRepository.save(membership);
    }

    public Membership upgradeToPremium(Long loginId, int amount) {
        Optional<Membership> membershipOpt = membershipRepository.findByLogin_Id(loginId);
        if (membershipOpt.isPresent()) {
            Membership membership = membershipOpt.get();
            membership.setMembershipType("premium");
            membership.setMembershipExpiryDate(LocalDate.now().plusYears(1)); // 1-year expiry
            membership.setPaymentDate(LocalDate.now());
            membership.setPaymentTime(LocalTime.now());
            membership.setAmount(amount);
            return membershipRepository.save(membership);
        }
        return null;
    }
}
