package hu.progmatic.kozos.felhasznalo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModositFelhasznalo {
    private Long felhasznaloId;
    private String nev;
    private UserType role;
    private String hitelesitoKod;
}
