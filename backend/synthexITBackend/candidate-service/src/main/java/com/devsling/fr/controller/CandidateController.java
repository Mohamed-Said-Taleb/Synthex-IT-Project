package com.devsling.fr.controller;

import com.devsling.fr.model.Candidate;
import com.devsling.fr.repository.CandidateRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/candidate")
public class CandidateController  {

    private final CandidateRepository candidateRepository;


    public CandidateController(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }


    @GetMapping("/all")
    public List<Candidate> getAll(){
      return   candidateRepository.findAll();
    }
}
