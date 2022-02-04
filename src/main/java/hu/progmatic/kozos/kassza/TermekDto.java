package hu.progmatic.kozos.kassza;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
public class TermekDto implements Serializable {
    private  Integer id;
    private  String megnevezes;
    @Min(0)
    private  Integer ar;
    @NotNull
    private  String vonalkod;
}
