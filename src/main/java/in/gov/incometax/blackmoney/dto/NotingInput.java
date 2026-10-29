package in.gov.incometax.blackmoney.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record NotingInput(
    @NotBlank @Size(max = 150) String name,
    @Size(max = 4000) String text
) {}
