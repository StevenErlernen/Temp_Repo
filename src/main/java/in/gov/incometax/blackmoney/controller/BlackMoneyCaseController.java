package in.gov.incometax.blackmoney.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.incometax.blackmoney.dto.CaseRequest;
import in.gov.incometax.blackmoney.dto.CaseResponse;
import in.gov.incometax.blackmoney.service.BlackMoneyCaseService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/v1/black-money-cases")
public class BlackMoneyCaseController {
    private final BlackMoneyCaseService service;
    private final ObjectMapper objectMapper;

    public BlackMoneyCaseController(BlackMoneyCaseService service, ObjectMapper objectMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
    }

    /**
     * One endpoint for both Save and Submit.
     * Multipart parts:
     * - request: JSON matching CaseRequest
     * - files: zero or more files
     * - categories: optional repeated strings aligned by file index
     * - fileDescriptions: optional repeated strings aligned by file index
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CaseResponse saveOrSubmit(
        @RequestPart("request") String requestJson,
        @RequestPart(value = "files", required = false) List<MultipartFile> files,
        @RequestPart(value = "categories", required = false) List<String> categories,
        @RequestPart(value = "fileDescriptions", required = false) List<String> fileDescriptions
    ) throws Exception {
        CaseRequest request = objectMapper.readValue(requestJson, new TypeReference<CaseRequest>() {});
        // Bean Validation on @RequestPart String does not validate the deserialized record automatically.
        var violations = jakarta.validation.Validation.buildDefaultValidatorFactory().getValidator().validate(request);
        if (!violations.isEmpty()) {
            String message = violations.stream().map(v -> v.getPropertyPath() + ": " + v.getMessage()).findFirst().orElse("Invalid request");
            throw new jakarta.validation.ConstraintViolationException(message, violations);
        }
        return service.saveOrSubmit(request, files, categories, fileDescriptions);
    }

    @GetMapping("/{caseId}")
    public CaseResponse get(@PathVariable Long caseId) { return service.get(caseId); }
}
