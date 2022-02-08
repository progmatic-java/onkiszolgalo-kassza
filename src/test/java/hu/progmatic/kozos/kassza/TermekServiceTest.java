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

        service.create(
                Termek.builder()
                        .megnevezes("spriteTeszt")
                        .ar(270)
                        .vonalkod("1263789060000000")
                        .mennyiseg(2)
                        .build()
        );
        Termek termek = service.findByNev("spriteTeszt");
        assertNotNull(termek.getId());
        assertEquals("spriteTeszt", termek.getMegnevezes());
        assertEquals(270, termek.getAr());
        assertEquals("1263789060000000", termek.getVonalkod());
        assertEquals(2, termek.getMennyiseg());
    }


    @Nested
    class modositasTorles {

        @BeforeEach
        void setUp() {
            service.create(
                    Termek.builder()
                            .megnevezes("ModositasTeszt")
                            .ar(270)
                            .vonalkod("1200000000")
                            .mennyiseg(2)
                            .build()
            );
        }

        @AfterEach
        void tearDown() {
            service.deleteByVonalkod("1200000000");
            assertNull(service.findByNev("ModositasTeszt"));
        }

        @Test
        @DisplayName("Termék mennyiségének módosítása")
        void termekModositasa() {
            service.modify("1200000000", 4);
            assertEquals(4, service.findByNev("ModositasTeszt").getMennyiseg());
        }


    }

    @Test
    void nemNullaMennyisegTest() {
        service.create(
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


    @Nested
    class unqieNevVonalkod {

        @BeforeEach
        void setUp() {
            service.create(
                    Termek.builder()
                            .megnevezes("Unique1Teszt")
                            .ar(270)
                            .vonalkod("1200000000")
                            .mennyiseg(2)
                            .build()

            );
            service.create(
                    Termek.builder()
                            .megnevezes("Unique2Teszt")
                            .ar(270)
                            .vonalkod("1100000000")
                            .mennyiseg(2)
                            .build()
            );
        }

        @AfterEach
        void tearDown() {
            service.deleteByVonalkod("1200000000");
            service.deleteByVonalkod("1100000000");
        }

        @Test
        void addUniqueNev() {
            String exceptionMessageMegnevezes = null;
            String exceptionMessageVonalkod = null;
            try{
                service.create(
                        Termek.builder()
                                .megnevezes("Unique1Teszt")
                                .ar(270)
                                .vonalkod("1300000000")
                                .mennyiseg(2)
                                .build()

                );
            }catch (FoglaltTermekException e){
                exceptionMessageMegnevezes = e.getBindingProperty().get("megnevezes");
                exceptionMessageVonalkod = e.getBindingProperty().get("vonalkod");
            }

            assertNull(exceptionMessageVonalkod);
            assertNotNull(exceptionMessageMegnevezes);
            assertEquals("Unique1Teszt nevű termék már van raktáron", exceptionMessageMegnevezes);
        }

        @Test
        void addUniqueVonalkod() {
            String exceptionMessageMegnevezes = null;
            String exceptionMessageVonalkod = null;
            try{
                service.create(
                        Termek.builder()
                                .megnevezes("Unique12Teszt")
                                .ar(270)
                                .vonalkod("1100000000")
                                .mennyiseg(2)
                                .build()

                );
            }catch (FoglaltTermekException e){
                exceptionMessageMegnevezes = e.getBindingProperty().get("megnevezes");
                exceptionMessageVonalkod = e.getBindingProperty().get("vonalkod");
            }

            assertNull(exceptionMessageMegnevezes);
            assertNotNull(exceptionMessageVonalkod);
            assertEquals("1100000000 számú vonalkód már foglalt", exceptionMessageVonalkod);
        }

        @Test
        void addUniqueVonalkodEsNev() {
            String exceptionMessageMegnevezes = null;
            String exceptionMessageVonalkod = null;
            try{
                service.create(
                        Termek.builder()
                                .megnevezes("Unique1Teszt")
                                .ar(270)
                                .vonalkod("1200000000")
                                .mennyiseg(2)
                                .build()

                );
            }catch (FoglaltTermekException e){
                exceptionMessageMegnevezes = e.getBindingProperty().get("megnevezes");
                exceptionMessageVonalkod = e.getBindingProperty().get("vonalkod");
            }

            assertNotNull(exceptionMessageMegnevezes);
            assertNotNull(exceptionMessageVonalkod);
            assertEquals("Unique1Teszt nevű termék már van raktáron", exceptionMessageMegnevezes);
            assertEquals("1200000000 számú vonalkód már foglalt", exceptionMessageVonalkod);
        }
    }
}