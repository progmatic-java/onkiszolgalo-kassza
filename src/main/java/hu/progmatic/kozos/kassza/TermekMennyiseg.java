package hu.progmatic.kozos.kassza;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TermekMennyiseg {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Min(0)
    private Integer mennyiseg;

    private boolean hitelesitesSzukseges;

    @ManyToOne
    private Termek termek;

    @ManyToOne
    private Kosar kosar;

    @OneToOne
    private Kosar utolsoElemKosar;
}


