package com.system.wood.infra.storage;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface StorageService {

    void store(MultipartFile file);

}
