package hu.progmatic.kozos.kassza;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Kosar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(
            length = 20,
            unique = true
    )
    @OneToMany(mappedBy = "kosar",cascade = CascadeType.ALL)
    private List<TermekMennyiseg> termekMennyisegek;

    @ManyToOne
    private Kassza kassza;
}
