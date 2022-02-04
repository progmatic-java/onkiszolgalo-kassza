package hu.progmatic.kozos.kassza;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Termek {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(unique = true)
    //@Pattern(regexp="^[A-Za-z]*$",message = "Invalid Input")
    private String megnevezes;
    @Min(0)
    private Integer ar;
    @NotNull
    @Pattern(regexp="^[0-9]*$",message = "A Vonalkód csak számokat tartalmazhat")
    @Column(unique = true)
    private String vonalkod;
    @Min(value = 0, message = "A termék mennyiség nem lehet negatív")
    @Max(value = 10000, message = "maximum 10000db terméket lehet felvenni")
    private Integer mennyiseg;


}

