package hu.progmatic.kozos.kassza;


import hu.progmatic.kozos.kassza.keszpenz.BedobottBankjegy;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Kosar {
    @Column(
            length = 20,
            unique = true

    )
    @Builder.Default
    @OneToMany(mappedBy = "kosar",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TermekMennyiseg> termekMennyisegek = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Builder.Default
    @OneToMany(mappedBy = "kosar",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BedobottBankjegy> bedobottBankjegyek = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private TermekMennyiseg utolsoHozzaadottTermekmennyiseg;

    @Enumerated(EnumType.STRING)
    private Hitelesites hitelesites;

    private LocalDateTime letrehozasDatuma;

    public static Integer kosarVegosszeg(Kosar kosar) {
        return kosar.getTermekMennyisegek().stream()
                .mapToInt(osszeg -> osszeg.getMennyiseg() * osszeg.getTermek().getAr())
                .sum();

    }
}
