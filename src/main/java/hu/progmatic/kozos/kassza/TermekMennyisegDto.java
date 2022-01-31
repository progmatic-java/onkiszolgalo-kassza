package hu.progmatic.kozos.kassza;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class TermekMennyisegDto implements Serializable {
    private final Integer mennyiseg;
    private final String nev;
    private final Integer ar;
}
