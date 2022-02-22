package hu.progmatic.kozos.kassza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public Termek create(Termek termek, TermekMentesCommand kepfeltoltesCommand) throws IOException {
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

    public void save(TermekMentesCommand termekmentes) {
        Termek termek = repository.getById(termekmentes.getId());
        termek.setMennyiseg(termekmentes.getMennyiseg());
        termek.setAr(termekmentes.getAr());
        termek.setMegnevezes(termekmentes.getMegnevezes());
        termek.setVonalkod(termekmentes.getVonalkod());
        termek.setHitelesitesSzukseges(termekmentes.isHitelesitesSzukseges());
        MultipartFile file = termekmentes.getFile();
        if (!file.isEmpty()) {
            try {
                Kep kep;
                if(termek.getKep() != null){
                    kep=termek.getKep();
                }
                else {
                    kep = new Kep();
                    termek.setKep(kep);
                }
                kep.setContentType(file.getContentType());
                kep.setKepAdat(file.getBytes());
                kep.setMeret(file.getSize());
            } catch (IOException e) {
                throw new KepFeltoltesHibaException();
            }
        }
    }

    public void deleteAll(List<Termek> termekek) {
        repository.deleteAll(termekek);
    }
}
