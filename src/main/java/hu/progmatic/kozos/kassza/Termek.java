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
    @Builder.Default
    private String megnevezes= "a";
    @Min(0)
    @Builder.Default
    private Integer ar = 10;
    @NotNull
    @Column(unique = true)
    @Builder.Default
    private String vonalkod = "10";
    @Min(0)
    @Builder.Default
    private Integer mennyiseg = 1;


}

