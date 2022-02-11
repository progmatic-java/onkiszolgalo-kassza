package hu.progmatic.kozos.kassza;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TermekMennyisegHozzaadasCommand {

  @NotEmpty
  private String vonalkod;
  @Max(value = 10000, message = "Túl nagy érték")
  @Positive(message = "Mennyiség nem lehet negatív vagy nulla")
  @NotNull(message = "Mennyiség nem lehet üres")
  private Integer mennyiseg;

}
