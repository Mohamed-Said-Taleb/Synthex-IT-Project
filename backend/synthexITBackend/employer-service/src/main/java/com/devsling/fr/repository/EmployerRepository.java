package com.devsling.fr.repository;

import com.devsling.fr.model.Employer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface EmployerRepository extends ReactiveCrudRepository<Employer,Long> {
    Mono<Employer> findByEmail(String email);
}
