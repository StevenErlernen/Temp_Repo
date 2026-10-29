package in.gov.incometax.blackmoney.dto;
import in.gov.incometax.blackmoney.domain.CaseStatus;
import java.time.Instant;
import java.util.List;
public record CaseResponse(Long caseId, String pan, String uniqueId, String assesseeName,
 String description, String sourceOfInfo, String assetIncomeDetails, String assignTo,
 String remarks, CaseStatus status, Instant createdAt, Instant updatedAt, List<NotingResponse> notings) {}
