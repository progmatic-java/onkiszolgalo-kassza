package hu.progmatic.kozos.kassza.keszpenz;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
    @NotNull(message ="A bankjegy mennyisége nem lehet 0!")
    @Min(0)
    @Max(100000)
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
