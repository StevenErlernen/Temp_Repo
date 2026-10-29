package in.gov.incometax.blackmoney.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "BM_CASE_ATTACHMENT")
public class CaseAttachment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ATTACHMENT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "NOTING_ID", nullable = false)
    private CaseNoting noting;

    @Column(name = "ORIGINAL_FILE_NAME", length = 255, nullable = false)
    private String originalFileName;

    @Column(name = "STORAGE_KEY", length = 500, nullable = false)
    private String storageKey;

    @Column(name = "CONTENT_TYPE", length = 150)
    private String contentType;

    @Column(name = "FILE_SIZE", nullable = false)
    private Long fileSize;

    @Column(name = "CATEGORY", length = 100)
    private String category;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Column(name = "UPLOADED_BY", length = 75, nullable = false)
    private String uploadedBy;

    @Column(name = "UPLOADED_AT", nullable = false)
    private Instant uploadedAt = Instant.now();

    public Long getId() { return id; }
    public CaseNoting getNoting() { return noting; }
    public void setNoting(CaseNoting noting) { this.noting = noting; }
    public String getOriginalFileName() { return originalFileName; }
    public void setOriginalFileName(String originalFileName) { this.originalFileName = originalFileName; }
    public String getStorageKey() { return storageKey; }
    public void setStorageKey(String storageKey) { this.storageKey = storageKey; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; }
    public Instant getUploadedAt() { return uploadedAt; }
}
