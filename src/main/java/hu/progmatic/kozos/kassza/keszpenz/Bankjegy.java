package hu.progmatic.kozos.kassza.keszpenz;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bankjegy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(unique = true)
    private Integer ertek;
    private Integer mennyiseg;

    public static Bankjegy bankjegyCloneFactory(Bankjegy bankjegy) {
        return Bankjegy.builder()
                .ertek(bankjegy.ertek)
                .mennyiseg(bankjegy.mennyiseg)
                .build();
    }

    public void noveles() {
        mennyiseg++;
    }

    public void csokkentes() {
        if (mennyiseg > 0){
            mennyiseg--;
        }
    }
}
