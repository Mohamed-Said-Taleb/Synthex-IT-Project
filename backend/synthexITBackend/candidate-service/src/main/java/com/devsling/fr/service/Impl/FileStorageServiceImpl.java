package com.devsling.fr.service.Impl;

import com.devsling.fr.dto.UploadImageResponse;
import com.devsling.fr.exceptions.ImageNotFoundException;
import com.devsling.fr.exceptions.ImageUploadException;
import com.devsling.fr.model.ImageData;
import com.devsling.fr.repository.ImageStorageRepository;
import com.devsling.fr.service.FileStorageService;
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
public class FileStorageServiceImpl implements FileStorageService {


    private final ImageStorageRepository imageStorageRepository;
    private final ImageUtils imageUtils;
    @Override
    public Mono<UploadImageResponse> uploadImage(MultipartFile file) throws IOException {
        long maxFileSizeBytes = 2 * 1024 * 1024;// 2 MB in bytes

        if (file.getSize() > maxFileSizeBytes) {
            return Mono.just(UploadImageResponse.builder()
                            .message(Constants.IMAGE_SIZE_ERROR)
                    .build());
        }

        byte[] compressedImage = imageUtils.compressImage(file.getBytes());
        return imageStorageRepository.save(ImageData.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .imageData(compressedImage)
                        .build())
                .map(savedImageData -> UploadImageResponse.builder()
                        .message(Constants.IMAGE_UPLOAD_SUCCESSFULLY)
                        .build())
                .switchIfEmpty(Mono.error(new ImageUploadException(Constants.UPLOAD_IMAGE_ERROR)));
    }



    @Override
    public Mono<byte[]> downloadImage(String fileName) {
        return imageStorageRepository.findByName(fileName)
                .map(dbImageData -> imageUtils.decompressImage(dbImageData.getImageData()))
                .switchIfEmpty(Mono.error(new ImageNotFoundException(IMAGE_NOT_FOUND + fileName)));
    }
}
