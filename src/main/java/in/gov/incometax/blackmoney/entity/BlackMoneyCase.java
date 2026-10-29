package in.gov.incometax.blackmoney.entity;

import in.gov.incometax.blackmoney.domain.CaseStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BM_CASE")
public class BlackMoneyCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CASE_ID")
    private Long id;

    @Column(name = "PAN", length = 10, nullable = false)
    private String pan;

    @Column(name = "UNIQUE_ID", length = 30)
    private String uniqueId;

    @Column(name = "NAME_OF_ASSESSEE", length = 60, nullable = false)
    private String assesseeName;

    @Column(name = "DESCRIPTION", length = 200, nullable = false)
    private String description;

    @Column(name = "SOURCE_OF_INFO", length = 50, nullable = false)
    private String sourceOfInfo;

    @Column(name = "DETAILS_OF_ASSET_INCOME", length = 150, nullable = false)
    private String assetIncomeDetails;

    @Column(name = "ASSIGN_TO", length = 20)
    private String assignTo;

    @Column(name = "REMARKS", length = 4000)
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 20, nullable = false)
    private CaseStatus status = CaseStatus.DRAFT;

    @Column(name = "CREATED_BY", length = 75, nullable = false)
    private String createdBy;

    @Column(name = "CREATION_DATE", nullable = false)
    private Instant creationDate;

    @Column(name = "LAST_UPDATED_BY", length = 75)
    private String lastUpdatedBy;

    @Column(name = "LAST_UPDATE_DATE")
    private Instant lastUpdateDate;

    @OneToMany(mappedBy = "blackMoneyCase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CaseNoting> notings = new ArrayList<>();

    @PrePersist
    void onCreate() {
        if (creationDate == null) creationDate = Instant.now();
        if (status == null) status = CaseStatus.DRAFT;
    }
    @PreUpdate
    void onUpdate() { lastUpdateDate = Instant.now(); }

    public void addNoting(CaseNoting noting) { noting.setBlackMoneyCase(this); notings.add(noting); }
    public void clearNotings() { notings.clear(); }
    public Long getId() { return id; }
    public String getPan() { return pan; }
    public void setPan(String pan) { this.pan = pan; }
    public String getUniqueId() { return uniqueId; }
    public void setUniqueId(String uniqueId) { this.uniqueId = uniqueId; }
    public String getAssesseeName() { return assesseeName; }
    public void setAssesseeName(String assesseeName) { this.assesseeName = assesseeName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getSourceOfInfo() { return sourceOfInfo; }
    public void setSourceOfInfo(String sourceOfInfo) { this.sourceOfInfo = sourceOfInfo; }
    public String getAssetIncomeDetails() { return assetIncomeDetails; }
    public void setAssetIncomeDetails(String assetIncomeDetails) { this.assetIncomeDetails = assetIncomeDetails; }
    public String getAssignTo() { return assignTo; }
    public void setAssignTo(String assignTo) { this.assignTo = assignTo; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public CaseStatus getStatus() { return status; }
    public void setStatus(CaseStatus status) { this.status = status; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public String getLastUpdatedBy() { return lastUpdatedBy; }
    public void setLastUpdatedBy(String lastUpdatedBy) { this.lastUpdatedBy = lastUpdatedBy; }
    public Instant getCreationDate() { return creationDate; }
    public Instant getLastUpdateDate() { return lastUpdateDate; }
    public List<CaseNoting> getNotings() { return notings; }
}
