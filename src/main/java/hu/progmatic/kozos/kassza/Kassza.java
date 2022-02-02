package hu.progmatic.kozos.kassza;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Kassza {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    @NonNull
    @Min(1)
    Integer nev;
}
