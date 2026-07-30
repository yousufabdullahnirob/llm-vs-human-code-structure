package com.bank.loan.controller;

import com.bank.loan.domain.LoanOfficer;
import com.bank.loan.dto.response.LoanOfficerResponse;
import com.bank.loan.repository.LoanOfficerRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/officers")
public class LoanOfficerController {

    @Autowired
    private LoanOfficerRepository loanOfficerRepository;

    @GetMapping
    public List<LoanOfficerResponse> getAllOfficers() {
        return loanOfficerRepository.findAll().stream()
                .map(o -> new LoanOfficerResponse(o.getId(), o.getName(), o.getEmail()))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<LoanOfficerResponse> createOfficer(@RequestBody LoanOfficer officer) {
        LoanOfficer saved = loanOfficerRepository.save(officer);
        LoanOfficerResponse response = new LoanOfficerResponse(saved.getId(), saved.getName(), saved.getEmail());
        return ResponseEntity.ok(response);
    }
}
