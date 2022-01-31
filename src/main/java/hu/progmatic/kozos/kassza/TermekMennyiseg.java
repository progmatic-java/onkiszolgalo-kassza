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
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Termek termek;
    @ManyToOne(cascade = CascadeType.ALL)
    private Kosar kosar;
}


