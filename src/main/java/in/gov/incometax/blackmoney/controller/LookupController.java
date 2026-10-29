package in.gov.incometax.blackmoney.controller;

import in.gov.incometax.blackmoney.dto.LookupOption;
import in.gov.incometax.blackmoney.service.LookupService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/lookups")
public class LookupController {
    private final LookupService service;
    public LookupController(LookupService service) { this.service = service; }

    @GetMapping("/source-of-info")
    public List<LookupOption> sourceOfInfo() { return service.sourceOfInfo(); }

    @GetMapping("/assign-to")
    public List<LookupOption> assignTo() { return service.assignees(); }
}
