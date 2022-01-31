package hu.progmatic.kozos.kassza;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TermekServiceTest {




    @Autowired TermekService service;


    @Test
    @DisplayName("Az alkalmazás indításakor léteznek termékek")
    void termekekLeteznek() {
        List<Termek> allTermek = service.findAll();
        assertThat(allTermek)
                .extracting(Termek::getMegnevezes)
                .containsAll(List.of("kenyér", "kóla"));
    }

    @Test
    @DisplayName("Rendelés hozzáadása")
    void termekHozzaadasa() {
        service.addTermek(
                Termek.builder()
                        .megnevezes("sprite")
                        .ar(270)
                        .vonalkod("126378906")
                        .mennyiseg(2)
                        .build()
        );
        Termek termek = service.findByNev("sprite");
        assertNotNull(termek.getId());
        assertEquals("sprite", termek.getMegnevezes());
        assertEquals(270, termek.getAr());
        assertEquals("126378906", termek.getVonalkod());
        assertEquals(2, termek.getMennyiseg());
    }

    @Test
    @DisplayName("Termék mennyiségének módosítása")
    void termekModositasa(){
        service.modify("12344345", 4);
        assertEquals(4, service.findByNev("kenyér").getMennyiseg());
    }

    @Test
    void termekTorlese() {
        service.deleteByVonalkod("12344345");
        assertNull(service.findByNev("kenyér"));



    }
}