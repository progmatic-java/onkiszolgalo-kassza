package hu.progmatic.kozos.kassza;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TermekMennyisegHozzaadasCommand {

  private Integer kosarId;
  @NotEmpty
  private String vonalkod;
  @Positive(message = "Mennyiség nem lehet negatív vagy nulla")
  @NotNull(message = "Mennyiség nem lehet üres")
  private Integer mennyiseg;

}
