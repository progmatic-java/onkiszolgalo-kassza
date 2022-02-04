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
    @OneToMany(mappedBy = "kosar",cascade = CascadeType.ALL, orphanRemoval = false)
    private List<TermekMennyiseg> termekMennyisegek;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private TermekMennyiseg utolsoHozzaadottTermekmennyiseg;

    @ManyToOne
    private Kassza kassza;
}
