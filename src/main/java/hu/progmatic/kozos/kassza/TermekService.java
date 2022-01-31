package hu.progmatic.kozos.kassza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TermekService {


    @Autowired
    private TermekRepository repository;


    public Termek save(Termek termek) {
        return repository.save(termek);
    }

    public Termek getById(Integer id) {
        return repository.getById(id);
    }

    public void saveAll(List<Termek> initItems) {
        repository.saveAll(initItems);
    }

    public List<Termek> findAllbyNev(String nev) {
        return repository.findAllByMegnevezesContains(nev);
    }

    public List<Termek> findAll() {
        return repository.findAll();
    }

    public Termek getByVonalkod(String vonalkod){
        return repository.findByVonalkod(vonalkod);
    }


    public Termek findByNev(String nev) {
        return repository.findByMegnevezes(nev);
    }

    public void addTermek(Termek termek) {
        repository.save(termek);
    }

    public void modify(String vonalkod, Integer mennyiseg) {
        Termek termek = getByVonalkod(vonalkod);
       if(termek != null){
           termek.setMennyiseg(mennyiseg);
       }
    }
}
