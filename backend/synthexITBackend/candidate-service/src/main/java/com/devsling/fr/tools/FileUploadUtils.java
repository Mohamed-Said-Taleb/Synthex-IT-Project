package com.devsling.fr.tools;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@Component
public class FileUploadUtils {

    private   Path foundFile;

    public  String saveFile(String fileName, MultipartFile multipartFile)
            throws IOException {
        Path uploadPath = Paths.get("Files-Upload");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileCode = RandomStringUtils.randomAlphanumeric(8);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileCode + "-" + fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileName, ioe);
        }

        return fileCode;
    }

    public Resource getFileAsResource(String fileCode) throws IOException {
        Path dirPath = Paths.get("Files-Upload");

        Files.list(dirPath).forEach(file -> {
            if (file.getFileName().toString().startsWith(fileCode)) {
                foundFile = file;
                return;
            }
        });

        if (foundFile != null) {
            return new UrlResource(foundFile.toUri());
        }

        return null;
    }

    //verify the delete file when we update the existing file not deleted
    public void deleteFile(String fileCode) throws IOException {
        Path dirPath = Paths.get("Files-Upload");

        Files.list(dirPath).forEach(file -> {
            if (file.getFileName().toString().startsWith(fileCode)) {
                try {
                    Files.deleteIfExists(file);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to delete file: " + file.toString(), e);
                }
            }
        });
    }
}
