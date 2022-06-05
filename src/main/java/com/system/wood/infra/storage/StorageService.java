package com.system.wood.infra.storage;

import com.system.wood.domain.container.Container;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String storeTestcase(MultipartFile file);

    String unzipFile(MultipartFile zipFile);

    void locateSkeletonCode(Container container);
}
