package hu.progmatic.kozos.kassza;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Kosar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    @Column(
            length = 20,
            unique = true
    )
    String nev;
    @OneToMany(mappedBy = "kosar",cascade = CascadeType.ALL)
    List<TermekMennyiseg> termekMennyisegek;

    @ManyToOne
    Kassza kassza;
}
