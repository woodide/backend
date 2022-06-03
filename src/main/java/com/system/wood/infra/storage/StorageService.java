package com.system.wood.infra.storage;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {

    String storeTestcase(MultipartFile file);

    String unzipFile(MultipartFile zipFile);
}
