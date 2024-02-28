package com.devsling.fr.service;

import java.io.IOException;

import com.devsling.fr.dto.UploadImageResponse;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

public interface  FileStorageService {
    Mono<UploadImageResponse> uploadImage(MultipartFile file) throws IOException;
    Mono<byte[]> downloadImage(String fileName);
}
