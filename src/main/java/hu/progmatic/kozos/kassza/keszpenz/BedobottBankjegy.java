package hu.progmatic.kozos.kassza.keszpenz;

import hu.progmatic.kozos.kassza.Kosar;
import hu.progmatic.kozos.kassza.keszpenz.Bankjegy;
import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BedobottBankjegy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    private Integer bedobottMenyiseg;

    @ManyToOne
    private Kosar kosar;

    @ManyToOne
    private Bankjegy bankjegy;

}
