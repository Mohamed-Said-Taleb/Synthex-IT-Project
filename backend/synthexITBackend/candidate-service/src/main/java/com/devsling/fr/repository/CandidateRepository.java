package com.devsling.fr.repository;

import com.devsling.fr.model.Candidate;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CandidateRepository extends ReactiveCrudRepository<Candidate,Long> {

    Mono<Candidate> findByEmail(String email);



}
