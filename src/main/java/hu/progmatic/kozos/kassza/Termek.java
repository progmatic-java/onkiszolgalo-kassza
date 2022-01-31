package hu.progmatic.kozos.kassza;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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
    private String megnevezes;
    @Min(0)
    private Integer ar;
    @NotNull
    @Column(unique = true)
    private String vonalkod;

    @Min(0)
    private Integer mennyiseg;


}

