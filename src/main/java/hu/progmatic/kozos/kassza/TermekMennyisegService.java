package hu.progmatic.kozos.kassza;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional
public class TermekMennyisegService {


    @Autowired
    private TermekMennyisegRepository termekMennyisegRepository;

    @Autowired
    private TermekRepository termekRepository;


    @Autowired
    TermekService termekService;

    public TermekMennyiseg save(TermekMennyiseg mennyiseg) {
        return termekMennyisegRepository.save(mennyiseg);
    }

    public TermekMennyiseg getById(Integer id) {
        return termekMennyisegRepository.getById(id);
    }

    public void saveAll(List<TermekMennyiseg> initItems) {
        termekMennyisegRepository.saveAll(initItems);
    }

    public void delete(Integer id) {
        termekMennyisegRepository.deleteById(id);
    }

    public void delete() {
        termekMennyisegRepository.deleteAll();
    }

    public Integer vegosszeg() {
        List<TermekMennyiseg> termekMennyisegek = termekMennyisegRepository.findAll();
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
        if (termekMennyisegRepository.findAll().isEmpty()) {
            TermekMennyiseg termekMennyiseg = TermekMennyiseg.builder()
                    .termek(termek)
                    .mennyiseg(mennyiseg)
                    .build();
            termekMennyisegRepository.save(termekMennyiseg);
            return;
        }

        List<TermekMennyiseg> termekMennyisegek = termekMennyisegRepository.findAll();
        termekMennyisegek.stream()
                .forEach(termekMennyiseg -> {
                    if (termekMennyiseg.getTermek().getVonalkod().equals(vonalkod)) {
                        TermekMennyiseg felulirando = TermekMennyiseg.builder()
                                .termek(termek)
                                .mennyiseg(termekMennyiseg.getMennyiseg() + mennyiseg)
                                .build();
                        termekMennyisegRepository.delete(termekMennyiseg);
                        termekMennyisegRepository.save(felulirando);
                        return;
                    } else {
                        TermekMennyiseg ujTermek = TermekMennyiseg.builder()
                                .termek(termek)
                                .mennyiseg(mennyiseg)
                                .build();
                        termekMennyisegRepository.save(ujTermek);
                        return;
                    }


                });
    }


    public List<TermekMennyiseg> findAll() {
        return termekMennyisegRepository.findAll();
    }

    public List<TermekMennyisegDto> findAllDto() {
        return termekMennyisegRepository.findAll().stream()
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
                        termekMennyisegRepository.findAll().stream()
                                .map(
                                        termekMennyiseg -> TermekMennyisegDto.builder()
                                                .mennyiseg(termekMennyisegRepository.getTermekMennyisegByKosar_Id(kosarId).getMennyiseg())
                                                .ar(termekMennyisegRepository.getTermekMennyisegByKosar_Id(kosarId).getTermek().getAr())
                                                .nev(termekMennyisegRepository.getTermekMennyisegByKosar_Id(kosarId).getTermek().getMegnevezes())
                                                .build()
                                )
                                .toList()
                )
                .vegosszeg(vegosszeg())
                .build();

    }
}

