package com.devsling.fr.repository;

import com.devsling.fr.model.ImageData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ImageStorageRepository extends ReactiveCrudRepository<ImageData,Long> {
    Mono<ImageData> findByName(String fileName);
}
