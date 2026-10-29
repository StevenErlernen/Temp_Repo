package in.gov.incometax.blackmoney.dto;

import in.gov.incometax.blackmoney.domain.CaseAction;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record CaseRequest(
    Long caseId,
    @NotBlank @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]", message = "PAN format is invalid") String pan,
    @Size(max = 30) String uniqueId,
    @NotBlank @Size(max = 60) String assesseeName,
    @NotBlank @Size(max = 200) String description,
    @NotBlank @Size(max = 50) String sourceOfInfo,
    @NotBlank @Size(max = 150) String assetIncomeDetails,
    @Size(max = 20) String assignTo,
    @Size(max = 4000) String remarks,
    @NotNull CaseAction action,
    @NotBlank @Size(max = 75) String actor,
    @Valid List<NotingInput> notings
) {}
