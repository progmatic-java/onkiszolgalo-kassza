package hu.progmatic.kozos.kassza;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class KasszaSzerviz {

    @Autowired
    KasszaRepository kasszaRepository;

    public Kassza save(Kassza kassza) {
        return kasszaRepository.saveAndFlush(kassza);
    }

    public Kassza getById(Integer id) {
        return kasszaRepository.getById(id);
    }

    public void saveAll(List<Kassza> initItems) {
        kasszaRepository.saveAllAndFlush(initItems);
    }

    public void delete(Integer id) {
        kasszaRepository.deleteById(id);
    }

    public void delete() {
        kasszaRepository.deleteAll();
    }
}
