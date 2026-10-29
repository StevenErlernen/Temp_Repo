package in.gov.incometax.blackmoney.dto;
import java.time.Instant;
public record AttachmentResponse(Long id, String originalFileName, String contentType, Long fileSize,
                                 String category, String description, String uploadedBy, Instant uploadedAt) {}
