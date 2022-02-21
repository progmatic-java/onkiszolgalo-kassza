package hu.progmatic.kozos.felhasznalo;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Felhasznalo {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false, unique = true)
  private String nev;

  private String jelszo;

  private String hitelesitoKod;

  @Enumerated(EnumType.STRING)
  private UserType role;
}