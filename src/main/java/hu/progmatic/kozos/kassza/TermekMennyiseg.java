package hu.progmatic.kozos.kassza;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TermekMennyiseg {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    @Min(0)
    Integer mennyiseg;
    @ManyToOne(cascade = CascadeType.PERSIST)
    Termek termek;
    @ManyToOne(cascade = CascadeType.ALL)
    Kosar kosar;
}


