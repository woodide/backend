package com.system.wood.infra.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String store(MultipartFile file);

}
