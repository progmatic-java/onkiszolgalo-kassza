package hu.progmatic.kozos.kassza;

import hu.progmatic.kozos.kassza.Termek;
import hu.progmatic.kozos.kassza.TermekSzerviz;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class TermekSzervizTest {


    @Autowired
    private TermekMennyisegService szerviz;


    @BeforeEach
    void setUp() {
        Kosar kosar = Kosar.builder()
                .nev("1-es kosar")
                .build();
        TermekMennyiseg termekMennyiseg = TermekMennyiseg.builder()
                .mennyiseg(1)
                .termek(Termek.builder()
                        .megnevezes("sör")
                        .ar(250)
                        .vonalkod("12345")
                        .build())
                .kosar(kosar)
                .build();
        TermekMennyiseg termekMennyiseg2 = TermekMennyiseg.builder()
                .mennyiseg(5)
                .termek(Termek.builder()
                        .megnevezes("bor")
                        .ar(750)
                        .vonalkod("55555")
                        .build())
                .kosar(kosar)
                .build();
        TermekMennyiseg termekMennyiseg3 = TermekMennyiseg.builder()
                .mennyiseg(2)
                .termek(Termek.builder()
                        .megnevezes("palinka")
                        .ar(1050)
                        .vonalkod("98765")
                        .build())
                .kosar(kosar)
                .build();

        szerviz.save(termekMennyiseg);
        szerviz.save(termekMennyiseg2);
        szerviz.save(termekMennyiseg3);
    }

    @AfterEach
    void tearDown() {
        szerviz.delete();
    }

    @Test
    @Disabled
    void vegosszeg() {
        assertEquals(6100, szerviz.vegosszeg());
    }
}