package in.gov.incometax.blackmoney.service;

import in.gov.incometax.blackmoney.domain.CaseAction;
import in.gov.incometax.blackmoney.domain.CaseStatus;
import in.gov.incometax.blackmoney.dto.*;
import in.gov.incometax.blackmoney.entity.*;
import in.gov.incometax.blackmoney.repository.BlackMoneyCaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@Service
public class BlackMoneyCaseService {
    private final BlackMoneyCaseRepository caseRepository;
    private final FileStorageService fileStorageService;

    public BlackMoneyCaseService(BlackMoneyCaseRepository caseRepository, FileStorageService fileStorageService) {
        this.caseRepository = caseRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public CaseResponse saveOrSubmit(CaseRequest request, List<MultipartFile> files,
                                     List<String> categories, List<String> fileDescriptions) {
        BlackMoneyCase entity = request.caseId() == null
            ? new BlackMoneyCase()
            : caseRepository.findById(request.caseId()).orElseThrow(() -> new NoSuchElementException("Case not found"));

        if (entity.getStatus() == CaseStatus.SUBMITTED)
            throw new IllegalStateException("Submitted cases cannot be changed");

        entity.setPan(request.pan().toUpperCase(Locale.ROOT));
        entity.setUniqueId(request.uniqueId() == null || request.uniqueId().isBlank() ? request.pan().toUpperCase(Locale.ROOT) : request.uniqueId());
        entity.setAssesseeName(request.assesseeName().trim());
        entity.setDescription(request.description().trim());
        entity.setSourceOfInfo(request.sourceOfInfo());
        entity.setAssetIncomeDetails(request.assetIncomeDetails().trim());
        entity.setAssignTo(request.assignTo());
        entity.setRemarks(request.remarks());
        if (entity.getId() == null) entity.setCreatedBy(request.actor());
        entity.setLastUpdatedBy(request.actor());

        // On update, replace the supplied noting list. Existing uploaded attachments are retained only
        // when editing isn't replacing notings; production clients should use dedicated attachment IDs.
        if (request.notings() != null) {
            entity.clearNotings();
            for (NotingInput input : request.notings()) {
                CaseNoting noting = new CaseNoting();
                noting.setName(input.name());
                noting.setText(input.text());
                entity.addNoting(noting);
            }
        }
        if (request.action() == CaseAction.SUBMIT) entity.setStatus(CaseStatus.SUBMITTED);
        else entity.setStatus(CaseStatus.DRAFT);

        // Persist case and notings first so generated IDs exist.
        entity = caseRepository.saveAndFlush(entity);

        if (files != null && !files.isEmpty()) {
            List<CaseNoting> notings = entity.getNotings();
            if (notings.isEmpty()) throw new IllegalArgumentException("Create at least one noting before attaching files");
            CaseNoting targetNoting = notings.get(0); // Files are attached to the first noting in this API version.
            for (int i = 0; i < files.size(); i++) {
                FileStorageService.StoredFile stored = fileStorageService.store(files.get(i));
                CaseAttachment attachment = new CaseAttachment();
                attachment.setOriginalFileName(stored.originalName());
                attachment.setStorageKey(stored.key());
                attachment.setContentType(stored.contentType());
                attachment.setFileSize(stored.size());
                attachment.setUploadedBy(request.actor());
                attachment.setCategory(categories != null && i < categories.size() ? categories.get(i) : null);
                attachment.setDescription(fileDescriptions != null && i < fileDescriptions.size() ? fileDescriptions.get(i) : null);
                targetNoting.addAttachment(attachment);
            }
            entity = caseRepository.saveAndFlush(entity);
        }
        return toResponse(entity);
    }

    @Transactional
    public CaseResponse get(Long id) {
        return toResponse(caseRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Case not found")));
    }

    private CaseResponse toResponse(BlackMoneyCase c) {
        List<NotingResponse> notings = c.getNotings().stream().map(n ->
            new NotingResponse(n.getId(), n.getName(), n.getText(), n.getAttachments().stream().map(a ->
                new AttachmentResponse(a.getId(), a.getOriginalFileName(), a.getContentType(), a.getFileSize(),
                    a.getCategory(), a.getDescription(), a.getUploadedBy(), a.getUploadedAt())).toList()
            )).toList();
        return new CaseResponse(c.getId(), c.getPan(), c.getUniqueId(), c.getAssesseeName(), c.getDescription(),
            c.getSourceOfInfo(), c.getAssetIncomeDetails(), c.getAssignTo(), c.getRemarks(), c.getStatus(),
            c.getCreationDate(), c.getLastUpdateDate(), notings);
    }
}
