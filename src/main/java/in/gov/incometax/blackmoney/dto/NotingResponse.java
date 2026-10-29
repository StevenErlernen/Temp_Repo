package in.gov.incometax.blackmoney.dto;
import java.util.List;
public record NotingResponse(Long id, String name, String text, List<AttachmentResponse> attachments) {}
