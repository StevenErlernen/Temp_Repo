package in.gov.incometax.blackmoney.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BM_CASE_NOTING")
public class CaseNoting {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTING_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CASE_ID", nullable = false)
    private BlackMoneyCase blackMoneyCase;

    @Column(name = "NOTING_NAME", length = 150, nullable = false)
    private String name;

    @Column(name = "NOTING_TEXT", length = 4000)
    private String text;

    @OneToMany(mappedBy = "noting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CaseAttachment> attachments = new ArrayList<>();

    public void addAttachment(CaseAttachment attachment) { attachment.setNoting(this); attachments.add(attachment); }
    public Long getId() { return id; }
    public BlackMoneyCase getBlackMoneyCase() { return blackMoneyCase; }
    public void setBlackMoneyCase(BlackMoneyCase blackMoneyCase) { this.blackMoneyCase = blackMoneyCase; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public List<CaseAttachment> getAttachments() { return attachments; }
}
