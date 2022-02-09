package hu.progmatic.kozos.kassza;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KosarViewDTO implements Serializable {

    private Integer kosarId;

    @NotNull
    @Builder.Default
    private Integer vegosszeg = 0;

    private TermekMennyisegDto utolsoHozzaadottTermekmennyisegDto;

    @Builder.Default
    private List<TermekMennyisegDto> termekMennyisegDtoList = new ArrayList<>();


}
