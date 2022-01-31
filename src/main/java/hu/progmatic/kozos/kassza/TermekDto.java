package hu.progmatic.kozos.kassza;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
public class TermekDto implements Serializable {
    private final Integer id;
    private final String megnevezes;
    @Min(0)
    private final Integer ar;
    @NotNull
    private final String vonalkod;
}
