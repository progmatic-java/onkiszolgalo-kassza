package hu.progmatic.kozos.kassza;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TermekMentesCommand {

    private MultipartFile file;

    private Integer id;
    @Column(unique = true)
    @NotEmpty(message = "A mező nem lehet üres")
    private String megnevezes;
    @NotNull(message = "A mező nem lehet üres")
    @Min(0)
    private Integer ar;
    @NotEmpty(message = "A mező nem lehet üres")
    @NotNull
    @Pattern(regexp="^[0-9]*$",message = "A Vonalkód csak számokat tartalmazhat")
    @Column(unique = true)
    @NotEmpty(message = "A mező nem lehet üres")
    private String vonalkod;
    @Min(value = 0, message = "A termék mennyiség nem lehet negatív")
    @Max(value = 10000, message = "maximum 10000db terméket lehet felvenni")
    @NotNull(message = "A mező nem lehet üres")
    private Integer mennyiseg;
    private boolean hitelesitesSzukseges = false;
}
