package hu.progmatic.kozos.kassza;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeszpenzDto {

    private Integer maradek;
    private Integer bedobottCimlet;
    private Integer kosarId;
    private String visszajaro;
    @Builder.Default
    private boolean nemTudVisszaadni = false;
}
