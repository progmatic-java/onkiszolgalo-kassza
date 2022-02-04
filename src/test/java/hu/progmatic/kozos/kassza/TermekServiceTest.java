package hu.progmatic.kozos.kassza;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TermekServiceTest {


    @Autowired
    TermekService service;


    @Test
    @DisplayName("Az alkalmazás indításakor léteznek termékek")
    void termekekLeteznek() {
        List<Termek> allTermek = service.findAll();
        assertThat(allTermek)
                .extracting(Termek::getMegnevezes)
                .containsAll(List.of("kenyér", "kóla"));
    }

    @Test
    @DisplayName("Termék hozzáadása")
    void termekHozzaadasa() {

        service.addTermek(
                Termek.builder()
                        .megnevezes("spriteTeszt")
                        .ar(270)
                        .vonalkod("126378906Teszt")
                        .mennyiseg(2)
                        .build()
        );
        Termek termek = service.findByNev("spriteTeszt");
        assertNotNull(termek.getId());
        assertEquals("spriteTeszt", termek.getMegnevezes());
        assertEquals(270, termek.getAr());
        assertEquals("126378906Teszt", termek.getVonalkod());
        assertEquals(2, termek.getMennyiseg());
    }


    @Nested
    class modositasTorles {

        @BeforeEach
        void setUp() {
            service.addTermek(
                    Termek.builder()
                            .megnevezes("ModositasTeszt")
                            .ar(270)
                            .vonalkod("12Teszt")
                            .mennyiseg(2)
                            .build()
            );
        }

        @AfterEach
        void tearDown() {
            service.deleteByVonalkod("12Teszt");
            assertNull(service.findByNev("ModositasTeszt"));
        }

        @Test
        @DisplayName("Termék mennyiségének módosítása")
        void termekModositasa() {
            service.modify("12Teszt", 4);
            assertEquals(4, service.findByNev("ModositasTeszt").getMennyiseg());
        }


    }

    @Test
    void nemNullaMennyisegTest() {
        service.addTermek(
                Termek.builder()
                        .megnevezes("probaNullaTermek")
                        .ar(270)
                        .vonalkod("11111111")
                        .mennyiseg(0)
                        .build()
        );
        Termek termek = service.findByNev("probaNullaTermek");
        assertNotNull(termek);
        List<Termek> termekek = service.findAllNotNullMennyiseg();
        assertThat(termekek).extracting(Termek::getMegnevezes).doesNotContain("probaNullaTermek");
        List<Termek> termekekWithNull = service.findAll();
        assertThat(termekekWithNull).extracting(Termek::getMegnevezes).contains("probaNullaTermek");
    }
}