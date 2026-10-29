package in.gov.incometax.blackmoney.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path root;
    private final Set<String> allowed;
    private final long maxBytes;

    public FileStorageService(@Value("${app.storage.root}") String root,
                              @Value("${app.allowed-extensions}") String extensions,
                              @Value("${app.max-file-size-bytes}") long maxBytes) throws IOException {
        this.root = Paths.get(root).toAbsolutePath().normalize();
        Files.createDirectories(this.root);
        this.allowed = Set.of(extensions.toLowerCase().split(","));
        this.maxBytes = maxBytes;
    }

    public StoredFile store(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("Uploaded file is empty");
        if (file.getSize() > maxBytes) throw new IllegalArgumentException("File exceeds 50 MB limit");
        String original = file.getOriginalFilename() == null ? "upload" : Paths.get(file.getOriginalFilename()).getFileName().toString();
        int dot = original.lastIndexOf('.');
        String ext = dot < 0 ? "" : original.substring(dot + 1).toLowerCase();
        if (!allowed.contains(ext)) throw new IllegalArgumentException("Unsupported file extension: " + ext);
        String key = UUID.randomUUID() + (ext.isBlank() ? "" : "." + ext);
        try {
            Path target = root.resolve(key).normalize();
            if (!target.startsWith(root)) throw new IllegalArgumentException("Invalid file path");
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return new StoredFile(original, key, file.getContentType(), file.getSize());
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to store uploaded file", ex);
        }
    }

    public record StoredFile(String originalName, String key, String contentType, long size) {}
}
