package com.expense.tracker.service.impl;

import com.expense.tracker.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FileServiceImpl implements FileService {

    private final String UPLOAD_DIR = "C:\\Users\\Aakash\\Desktop\\files\\";

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);


    @Override
    public String uploadFile(MultipartFile file) {
        try {
            // Validate file type
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !isValidFileType(originalFilename)) {
                logger.warn("Invalid file type: {}", originalFilename);
                throw new RuntimeException("Invalid file type. Allowed types are: .txt, .pdf, .jpg");
                // return "Invalid file type. Allowed types are: .txt, .pdf, .jpg";
            }
    
            // Validate file size (max 5MB)
            if (file.getSize() > 5 * 1024 * 1024) { // 5MB
                logger.warn("File size exceeds size limit: {} bytes", file.getSize());
                throw new RuntimeException("File size exceeds the limit of 5MB");
            }
    
            // Ensure the filename is clean
            String sanitizedFilename = Paths.get(originalFilename).getFileName().toString();
    
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
    
            String filePath = UPLOAD_DIR + sanitizedFilename;
            file.transferTo(new File(filePath));

            logger.info("File uploaded successfully: {}" , filePath);
            return "File uploaded successfully: " + filePath;
        } catch (IOException e) {
            logger.error("Failed to upload file: {}", e.getMessage());
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }
    
    private boolean isValidFileType(String filename) {
        String[] allowedExtensions = {".txt", ".pdf", ".jpg"};
        for (String extension : allowedExtensions) {
            if (filename.toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
    

    @Override
    public Resource downloadFile(String filename) {
        logger.info("File download requested for: {}", filename);
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                logger.info("File downloaded successfully: {}", filename);
                return resource;
            } else {
                logger.error("File is not readable: {}", filename);
                throw new RuntimeException("File not found or not readable: " + filename);
            }
        } catch (MalformedURLException e) {
            logger.error("Error while reading file: {}", e.getMessage());
            throw new RuntimeException("Error resolving file path: " + e.getMessage());
        }
    }
}
