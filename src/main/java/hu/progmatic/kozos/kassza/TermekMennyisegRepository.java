package hu.progmatic.kozos.kassza;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TermekMennyisegRepository extends JpaRepository<TermekMennyiseg,Integer>{
     TermekMennyiseg getTermekMennyisegByKosar(Kosar kosar);
}
