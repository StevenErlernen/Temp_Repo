package in.gov.incometax.blackmoney.service;

import in.gov.incometax.blackmoney.dto.LookupOption;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LookupService {
    // Replace these illustrative values with approved master tables/configuration.
    public List<LookupOption> sourceOfInfo() {
        return List.of(
            new LookupOption("PUBLIC_DOMAIN", "Public Domain"),
            new LookupOption("OTHER_AGENCY", "Other Agency"),
            new LookupOption("MLAT", "MLAT"),
            new LookupOption("CRS", "CRS"),
            new LookupOption("FATCA", "FATCA"),
            new LookupOption("TIEA", "TIEA"),
            new LookupOption("DTAA", "DTAA"),
            new LookupOption("FIU", "FIU"),
            new LookupOption("OPEN_ENQUIRY", "Open Enquiry"),
            new LookupOption("SURVEY", "Survey"),
            new LookupOption("SEARCH", "Search"),
            new LookupOption("MULTILATERAL", "Multilateral"),
            new LookupOption("OTHERS", "Others"),
            new LookupOption("TEP", "TEP"),
            new LookupOption("INFORMANT", "Informant"),
            new LookupOption("FI_CASE", "FI Case"),
            new LookupOption("PANAMA_LEAKS", "Panama Leaks"),
            new LookupOption("PANDORA_LEAKS", "Pandora Leaks"),
            new LookupOption("PARADISE_PAPERS", "Paradise Papers"),
            new LookupOption("SWISS_LEAKS", "Swiss Leaks"),
            new LookupOption("DUBAI_LEAKS", "Dubai Leaks"),
            new LookupOption("EGMONT_REQUEST", "Egmont Request")
        );
    }

    public List<LookupOption> assignees() {
        // Replace with the authorised employee/position lookup integration.
        return List.of(
            new LookupOption("IO19_DELHI", "IO 19, Delhi"),
            new LookupOption("PUBLIC_DOMAIN", "Public Domain")
        );
    }
}
