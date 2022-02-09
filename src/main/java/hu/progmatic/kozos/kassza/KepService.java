package hu.progmatic.kozos.kassza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Transactional
@Service
public class KepService {

    @Autowired
    private KepRepository repository;

    public List<KepDto> getAllKepDto() {
        return repository.findAll().stream()
                .map(
                        kep -> KepDto.builder()
                                .id(kep.getId())
                                .megnevezes(kep.getMegnevezes())
                                .meret(meretFormazas(kep.getMeret()))
                                .build()
                ).toList();
    }

    public String meretFormazas(Long meret) {
        if (meret > 1024 * 1024) {
            return (meret / (1024 * 1024)) + " MB";
        }
        if (meret > 1024) {
            return (meret / 1024) + " KB";
        }
        return meret + " B";
    }

    public KepMegjelenitesDto getKepMegjelenitesDto(Integer kepId) {
        Kep kep = repository.getById(kepId);
        return KepMegjelenitesDto.builder()
                .contentType(kep.getContentType())
                .kepAdat(kep.getKepAdat())
                .build();
    }

    public void saveKep(KepfeltoltesCommand kepFeltoltesCommand) {
        try {
            MultipartFile kepFile = kepFeltoltesCommand.getFile();
            Kep kep = Kep.builder()
                    .contentType(kepFile.getContentType())
                    .meret(kepFile.getSize())
                    .kepAdat(kepFile.getBytes())
                    .megnevezes(kepFeltoltesCommand.getNev())
                    .build();
            repository.save(kep);
        } catch (IOException e) {
            throw new KepFeltoltesHibaException();
        }
    }

    public void deleteKep(Integer kepId) {
        repository.deleteById(kepId);
    }
}
