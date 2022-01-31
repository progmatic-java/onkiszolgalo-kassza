package hu.progmatic.kozos.kassza;

import hu.progmatic.kozos.kassza.TermekDto;
import hu.progmatic.kozos.kassza.TermekMennyisegDto;
import lombok.*;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KosarViewDTO implements Serializable {
    @NotNull
    Integer vegosszeg;

    TermekDto termekDto;

    List<TermekDto> termekDtoList;

    List<TermekMennyisegDto> termekMennyisegDtoList;





}
