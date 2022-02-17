package hu.progmatic.kozos.kassza;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TermekMennyisegDto implements Serializable {
    private Integer termekMennyisegId;
    private Integer mennyiseg;
    private String nev;
    private Integer ar;
    private byte[] kepAdat;
    private String contentType;
}
