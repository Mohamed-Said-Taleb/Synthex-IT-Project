package com.devsling.fr.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

public interface  FileStorageService {
    Mono<String> uploadImage(MultipartFile file) throws IOException;
    Mono<byte[]> downloadImage(String fileName);
}
