package hu.progmatic.kozos.kassza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TermekService {


    @Autowired
    private TermekRepository repository;


    public Termek add(Termek termek) {
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

    public Termek getByVonalkod(String vonalkod) {
        return repository.findByVonalkod(vonalkod);
    }


    public Termek findByNev(String nev) {
        return repository.findByMegnevezes(nev);
    }

    /*public void addTermek(Termek termek) {
        repository.save(termek);
    }*/

    public void modify(String vonalkod, Integer mennyiseg) {
        Termek termek = getByVonalkod(vonalkod);
        if (termek != null) {
            termek.setMennyiseg(mennyiseg);
        }
    }

    public List<Termek> findAllNotNullMennyiseg() {
        return repository.findAllByMennyisegGreaterThan(0);
    }

    public void deleteByVonalkod(String vonalkod) {
        repository.deleteByVonalkod(vonalkod);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public void validacio(Termek termek) {
        Map<String, String> uniqueItemMap = new HashMap<>();
        if (repository.findByMegnevezes(termek.getMegnevezes()) != null) {
            uniqueItemMap.put("megnevezes", String.format("%s nevű termék már van raktáron", termek.getMegnevezes()));
        }
        if (repository.findByVonalkod(termek.getVonalkod()) != null) {
            uniqueItemMap.put("vonalkod", String.format("%s számú vonalkód már foglalt", termek.getVonalkod()));
        }
        if (!uniqueItemMap.isEmpty()) {
            throw new FoglaltTermekException(uniqueItemMap);
        }
    }

    public Termek create(Termek termek, KepfeltoltesCommand kepfeltoltesCommand) throws IOException {
        termek.setId(null);
        try {
            MultipartFile file = kepfeltoltesCommand.getFile();

            Kep kep = Kep.builder()
                    .contentType(file.getContentType())
                    .kepAdat(file.getBytes())
                    .meret(file.getSize())
                    .build();
            termek.setKep(kep);
        } catch (IOException e) {
            throw new KepFeltoltesHibaException();
        }
        return repository.save(termek);
    }

    public Termek create(Termek termek) {
        termek.setId(null);
        return repository.save(termek);
    }

    public KepMegjelenitesDto getKepMegjelenitesDto(Integer id) {
        Kep kep = repository.getById(id).getKep();
        return KepMegjelenitesDto.builder()
                .contentType(kep.getContentType())
                .kepAdat(kep.getKepAdat())
                .build();

    }
}
