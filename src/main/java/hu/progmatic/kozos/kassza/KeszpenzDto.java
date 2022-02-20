package hu.progmatic.kozos.kassza;

import hu.progmatic.kozos.kassza.keszpenz.EnabledBankjegyDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeszpenzDto {

    private Integer maradek;
    private Integer bedobottCimlet;
    private Integer vegosszeg;
    private Integer kosarId;
    private String visszajaro;
    private List<EnabledBankjegyDto> enabledBankjegyek;
    @Builder.Default
    private boolean fizetesMegszakitas = false;
}

