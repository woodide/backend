package com.system.wood.infra.storage;

import com.system.wood.domain.assigment.Assignment;
import com.system.wood.domain.container.Container;
import com.system.wood.global.error.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@Service
public class FileSystemService implements StorageService {

    @Value("${file.uploadfile-path}")
    private String rootLocation;

    @Value("${file.testcase-path}")
    private String testcasePath;

    @Override
    public String storeTestcase(MultipartFile file) {

        Path fileUploadUrl = Path.of(testcasePath + createStoreFileName(file.getOriginalFilename()));

        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            Files.copy(file.getInputStream(), fileUploadUrl);

            return fileUploadUrl.toString();
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    public String unzipFile(MultipartFile zipFile) {

        if(isNotArchive(zipFile))
            throw new StorageException("zip파일이 아닙니다.");

        Path targetDir = Path.of(rootLocation, UUID.randomUUID().toString());

        try (ZipInputStream zis = new ZipInputStream(zipFile.getInputStream())) {

            // list files in zip
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {

                boolean isDirectory = false;
                if (zipEntry.getName().endsWith(File.separator)) {
                    isDirectory = true;
                }

                Path newPath = zipSlipProtect(zipEntry, targetDir);
                if (isDirectory) {
                    Files.createDirectories(newPath);
                } else {
                    if (newPath.getParent() != null) {
                        if (Files.notExists(newPath.getParent())) {
                            Files.createDirectories(newPath.getParent());
                        }
                    }
                    // copy files
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);
                }

                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return targetDir.toString()+ "/"+ deleteExt(zipFile.getOriginalFilename());
    }

    public void locateSkeletonCode(Container container) {
        String target = container.getPath() + "/workspace";
        String sourcePath = container.getAssignment().getUploadUrl();

        String command = new StringBuilder("cp -R ")
                .append(sourcePath)
                .append("/. ")
                .append(target)
                .toString();
        try {
            Process process = Runtime.getRuntime()
                    .exec(command);
        } catch (IOException e) {
            e.printStackTrace();
            throw new StorageException("스켈레톤 코드를 복사할 수 없습니다.");
        }
    }

    public String locateTarget(Container container, Assignment assignment) {
        String sourcePathDir = container.getPath() + "/workspace";
        String sourceFileName = assignment.getTargetFileName();
        String assignmentDir = assignment.getUploadUrl();
        String targetFileName = container.getStudent().getStudentNumber() + "." + extractExt(sourceFileName);

        Path source = Path.of(sourcePathDir, sourceFileName);
        Path target = Path.of(assignmentDir, targetFileName);

        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
            throw new StorageException("제출 파일을 복사할 수 없습니다.");
        }
        log.info("파일 이동 성공");
        return targetFileName;
    }

    private boolean isNotArchive(MultipartFile file) {

        // file signature(magic numbers)를 사용한 분류
        byte[][] criterion = {{0x50, 0x4B, 0x03, 0x04}, {0x50, 0x4B, 0x05, 0x06}, {0x50, 0x4B, 0x07, 0x08}};
        int fileSignature = 0;
        try {
            byte[] bytes = file.getInputStream().readNBytes(4);
            for (byte[] temp : criterion) {
                if(Arrays.equals(temp, bytes))
                    return false;
            }
        } catch (IOException e) {
            // handle if you like
        }
        return true;
    }

    private Path zipSlipProtect(ZipEntry zipEntry, Path targetDir)
            throws IOException {

        // test zip slip vulnerability
        Path targetDirResolved = targetDir.resolve(zipEntry.getName());

        // make sure normalized file still has targetDir as its prefix
        // else throws exception
        Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("Bad zip entry: " + zipEntry.getName());
        }
        return normalizePath;
    }

    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        if(ext.isEmpty())
            return uuid;
        else
            return uuid + "." + ext;
    }

    private String deleteExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(0, pos);
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        if(pos == -1)
            return "";
        else
            return originalFilename.substring(pos + 1);
    }
}
