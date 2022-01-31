package hu.progmatic.kozos.kassza;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional
public class TermekMennyisegService {


    @Autowired
    private TermekMennyisegRepository repository;

    @Autowired
    private TermekRepository termekRepository;


    @Autowired
    TermekService termekService;

    public TermekMennyiseg save(TermekMennyiseg mennyiseg) {
        return repository.save(mennyiseg);
    }

    public TermekMennyiseg getById(Integer id) {
        return repository.getById(id);
    }

    public void saveAll(List<TermekMennyiseg> initItems) {
        repository.saveAll(initItems);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public void delete() {
        repository.deleteAll();
    }

    public Integer vegosszeg() {
        List<TermekMennyiseg> termekMennyisegek = repository.findAll();
        return termekMennyisegek.stream()
                .mapToInt(termekMennyiseg -> termekMennyiseg.getMennyiseg() * termekMennyiseg.getTermek().getAr())
                .sum();
    }

    public void termekHozzaadasa(String vonalkod, Integer mennyiseg) {
        Termek termek = termekService.getByVonalkod(vonalkod);
        if (termek.getMennyiseg() < mennyiseg) {
            throw new NincsElegRaktarKeszletException();
        }
        termek.setMennyiseg(termek.getMennyiseg() - mennyiseg);
        if (repository.findAll().isEmpty()) {
            TermekMennyiseg termekMennyiseg = TermekMennyiseg.builder()
                    .termek(termek)
                    .mennyiseg(mennyiseg)
                    .build();
            repository.save(termekMennyiseg);
            return;
        }

        List<TermekMennyiseg> termekMennyisegek = repository.findAll();
        termekMennyisegek.stream()
                .forEach(termekMennyiseg -> {
                    if (termekMennyiseg.getTermek().getVonalkod().equals(vonalkod)) {
                        TermekMennyiseg felulirando = TermekMennyiseg.builder()
                                .termek(termek)
                                .mennyiseg(termekMennyiseg.getMennyiseg() + mennyiseg)
                                .build();
                        repository.delete(termekMennyiseg);
                        repository.save(felulirando);
                        return;
                    } else {
                        TermekMennyiseg ujTermek = TermekMennyiseg.builder()
                                .termek(termek)
                                .mennyiseg(mennyiseg)
                                .build();
                        repository.save(ujTermek);
                        return;
                    }


                });
    }


    public List<TermekMennyiseg> findAll() {
        return repository.findAll();
    }

    public List<TermekMennyisegDto> findAllDto() {
        return repository.findAll().stream()
                .map(termekMennyiseg -> TermekMennyisegDto.builder()
                        .ar(termekMennyiseg.getTermek().getAr())
                        .nev(termekMennyiseg.getTermek().getMegnevezes())
                        .mennyiseg(termekMennyiseg.getMennyiseg())
                        .build())
                .toList();
    }

    public KosarViewDTO kosarViewDTO(Integer kosarId) {
        return KosarViewDTO.builder()
                .termekDtoList(
                        termekRepository.findAll().stream()
                                .map(
                                        termek -> TermekDto.builder()
                                                .megnevezes(termek.getMegnevezes())
                                                .build()
                                )
                                .toList()
                )
                .termekMennyisegDtoList(
                        repository.findAll().stream()
                                .map(
                                        termekMennyiseg -> TermekMennyisegDto.builder()
                                                .mennyiseg(repository.getTermekMennyisegByKosar_Id(kosarId).getMennyiseg())
                                                .ar(repository.getTermekMennyisegByKosar_Id(kosarId).getTermek().getAr())
                                                .nev(repository.getTermekMennyisegByKosar_Id(kosarId).getTermek().getMegnevezes())
                                                .build()
                                )
                                .toList()
                )
                .vegosszeg(vegosszeg())
                .build();

    }
}

