package hu.progmatic.kozos.kassza;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class TermekMennyisegHozzaadasCommand {
  @NotEmpty
  private String vonalkod;
  @Positive(message = "Mennyiség nem lehet negatív vagy nulla")
  @NotNull(message = "Mennyiség nem lehet üres")
  private Integer mennyiseg;
}
