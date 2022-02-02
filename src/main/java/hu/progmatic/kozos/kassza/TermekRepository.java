package hu.progmatic.kozos.kassza;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TermekRepository extends JpaRepository<Termek,Integer> {
    List<Termek> findAllByMegnevezesContains(String nev);

    Termek findByVonalkod(String vonalkod);

    Termek findByMegnevezes(String nev);

    void deleteByVonalkod(String vonalkod);

    List<Termek> findAllByMennyisegGreaterThan(Integer minMennyiseg);
}
