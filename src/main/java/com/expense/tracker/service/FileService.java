package com.expense.tracker.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

public interface FileService {
    String uploadFile(MultipartFile file);
    Resource downloadFile(String filename);
}