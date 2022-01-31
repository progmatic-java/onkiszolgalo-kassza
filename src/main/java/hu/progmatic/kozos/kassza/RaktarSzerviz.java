package hu.progmatic.kozos.kassza;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RaktarSzerviz {

    @Autowired
    RaktarRepository raktarRepository;

    public Raktar save(Raktar raktar) {
        return raktarRepository.saveAndFlush(raktar);
    }

    public Raktar getById(Integer id) {
        return raktarRepository.getById(id);
    }
}
