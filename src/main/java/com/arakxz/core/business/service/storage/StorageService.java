package com.arakxz.core.business.service.storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {

    public static final String UUID_FOLDER = "0A1BDD85-9B77-4CE9-82C9-A9D84BD7E944";

    private final Path location;

    @Autowired
    public StorageService() {
        String folder = UUID.randomUUID().toString().toUpperCase();
        this.location = Paths.get(
        		System.getProperty("java.io.tmpdir") + UUID_FOLDER + System.getProperty("file.separator") + folder
        );
    }

    public void init() {
        try {
            Files.createDirectories(this.location);
        } catch (IOException error) {
            throw new StorageServiceException("Could not initialize storage", error);
        }
    }

    public void store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageServiceException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageServiceException(
                        "Cannot store file with relative path outside current directory " + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(
                        inputStream,
                        this.location.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING
                );
            }
        } catch (IOException error) {
            throw new StorageServiceException("Failed to store file " + filename, error);
        }

    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(location.toFile());
    }

    public Path load(String filename) {
        return this.location.resolve(filename);
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.location, 1).filter(path -> !path.equals(this.location))
                    .map(this.location::relativize);
        } catch (IOException error) {
            throw new StorageServiceException("Failed to read stored files", error);
        }
    }

    public Resource loadAsResource(String filename) throws FileNotFoundException {
        try {

            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            }

            throw new FileNotFoundException("Could not read file: " + filename);

        } catch (MalformedURLException error) {
            throw new FileNotFoundException("Could not read file: " + filename);
        }
    }

}
