package com.devsling.fr.tools;

import com.devsling.fr.exceptions.FIleUploadException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

@Component
public class FileUploadUtils {

    private   Path foundFile;

    public String saveFile(String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(Constants.FILE_PATH);

        long maxFileSizeBytes = 2 * 1024 * 1024;
        if (multipartFile.getSize() > maxFileSizeBytes) {
            throw new FIleUploadException("File size exceeds the maximum allowed limit of 2 MB.");
        }

        List<String> allowedFormats = Arrays.asList("rtf", "dot", "doc", "docx", "pdf", "odt", "wpd", "ods", "gif", "png", "jpg", "jpeg", "bmp");
        String fileExtension = FilenameUtils.getExtension(fileName).toLowerCase();
        if (!allowedFormats.contains(fileExtension)) {
            throw new FIleUploadException("Invalid file format. Allowed formats: " + allowedFormats);
        }

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = multipartFile.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ioe) {
                throw new FIleUploadException("Could not save file: " + fileName);
            }

            return fileName;
        } catch (IOException e) {
            throw new FIleUploadException("Failed to create directories: " + uploadPath);
        }
    }


    public Resource getFileAsResource(String fileCode) throws IOException {
        Path dirPath = Paths.get(Constants.FILE_PATH);

        Files.list(dirPath).forEach(file -> {
            if (file.getFileName().toString().startsWith(fileCode)) {
                foundFile = file;
            }
        });

        if (foundFile != null) {
            return new UrlResource(foundFile.toUri());
        }

        return null;
    }
    public void deleteFile(String fileName) {
        Path dirPath = Paths.get(Constants.FILE_PATH);

        try {
            Files.list(dirPath).forEach(file -> {
                if (file.getFileName().toString().startsWith(fileName)) {
                    try {
                        Files.deleteIfExists(file);
                    } catch (IOException e) {
                        throw new FIleUploadException("Failed to delete file: " + file);
                    }
                }
            });
        } catch (IOException e) {
            throw new FIleUploadException("Failed to list files in directory: " + dirPath);
        }}
}
