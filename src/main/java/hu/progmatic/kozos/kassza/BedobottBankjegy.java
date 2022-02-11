package hu.progmatic.kozos.kassza;

import lombok.*;

import javax.persistence.*;
import java.util.List;

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
