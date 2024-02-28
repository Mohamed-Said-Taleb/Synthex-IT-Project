package com.devsling.fr.service;

import com.devsling.fr.exceptions.ImageNotFoundException;
import com.devsling.fr.exceptions.ImageUploadException;
import com.devsling.fr.model.ImageData;
import com.devsling.fr.repository.FileStorageRepository;
import com.devsling.fr.tools.Constants;
import com.devsling.fr.tools.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static com.devsling.fr.tools.Constants.IMAGE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService{


    private final FileStorageRepository fileStorageRepository;
    @Override
    public Mono<String> uploadImage(MultipartFile file) throws IOException {
        return fileStorageRepository.save(ImageData.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .imageData(ImageUtils.compressImage(file.getBytes())).build())
                .flatMap(imageData -> {
                    if (imageData != null) {
                        return Mono.just(Constants.IMAGE_UPLOAD_SUCCESSFULLY+ file.getOriginalFilename());
                    } else {
                        return Mono.error(new ImageUploadException(Constants.UPLOAD_IMAGE_ERROR));
                    }
                });
    }

    @Override
    public Mono<byte[]> downloadImage(String fileName) {
        return fileStorageRepository.findByName(fileName)
                .map(dbImageData -> ImageUtils.decompressImage(dbImageData.getImageData()))
                .switchIfEmpty(Mono.error(new ImageNotFoundException(IMAGE_NOT_FOUND + fileName)));
    }
}
