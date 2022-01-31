package hu.progmatic.kozos.kassza;

import hu.progmatic.kozos.kassza.Kosar;
import hu.progmatic.kozos.kassza.Termek;
import hu.progmatic.kozos.kassza.TermekMennyiseg;
import hu.progmatic.kozos.kassza.TermekMennyisegService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TermekMennyisegServiceTest {

    @Autowired
    private TermekMennyisegService szerviz;

    @Test
    void hozzaAdas() {
        szerviz.termekHozzaadasa("12345",1);
        szerviz.termekHozzaadasa("12345",3);
        szerviz.termekHozzaadasa("12335",3);

        List<TermekMennyiseg> termekMennyisegek = szerviz.findAll();
        assertThat(termekMennyisegek.stream().map(termekMennyiseg -> termekMennyiseg.getTermek().megnevezes))
                .containsExactlyInAnyOrder("víz","kóla");
    }
}