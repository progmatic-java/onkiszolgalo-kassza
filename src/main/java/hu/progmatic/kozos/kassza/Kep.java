package hu.progmatic.kozos.kassza;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Kep {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String contentType;

    private Long meret;

    private String megnevezes;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    byte[] kepAdat;

    @OneToOne
    Termek termek;
}
