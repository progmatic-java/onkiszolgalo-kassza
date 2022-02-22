package hu.progmatic.kozos.kassza.servicetest;

import hu.progmatic.kozos.kassza.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KosarServiceTest {

    @Autowired
    private KosarService kosarService;

    @Autowired
    private TermekService termekService;


    @Test
    void kosarLetrehozas() {
        KosarViewDTO kosarViewDTO = kosarService.kosarViewCreate();
        assertNotNull(kosarViewDTO.getKosarId());
    }

    @Nested
    class HozzaadTest {

        KosarViewDTO kosarViewDTO;
        List<Termek> termekek = List.of(
                Termek.builder().vonalkod("01").mennyiseg(10).megnevezes("Teszt1").ar(1000).build(),
                Termek.builder().vonalkod("02").mennyiseg(10).megnevezes("Teszt2").ar(1000).build(),
                Termek.builder().vonalkod("03").mennyiseg(10).megnevezes("Teszt3").ar(1000).build(),
                Termek.builder().vonalkod("04").mennyiseg(10).megnevezes("Teszt4").ar(1000).build(),
                Termek.builder().vonalkod("05").mennyiseg(10).megnevezes("Teszt5").ar(1000).build(),
                Termek.builder().vonalkod("06").mennyiseg(10).megnevezes("Teszt6").ar(1000).build(),
                Termek.builder().vonalkod("07").mennyiseg(10).megnevezes("Teszt7").ar(1000).build(),
                Termek.builder().vonalkod("08").mennyiseg(10).megnevezes("Teszt8").ar(1000).build(),
                Termek.builder().vonalkod("09").mennyiseg(10).megnevezes("Teszt9").ar(1000).build(),
                Termek.builder().vonalkod("10").mennyiseg(10).megnevezes("Teszt10").ar(1000).build(),
                Termek.builder().vonalkod("11").mennyiseg(10).megnevezes("Teszt11").ar(1000).build(),
                Termek.builder().vonalkod("12").mennyiseg(10).megnevezes("Teszt12").ar(1000).build()
        );

        private List<TermekMennyisegHozzaadasCommand> getTermekMennyisegHozzaadasCommandList() {
            return List.of(
                    TermekMennyisegHozzaadasCommand.builder().vonalkod("01").mennyiseg(1).build(),
                    TermekMennyisegHozzaadasCommand.builder().vonalkod("02").mennyiseg(1).build(),
                    TermekMennyisegHozzaadasCommand.builder().vonalkod("03").mennyiseg(1).build(),
                    TermekMennyisegHozzaadasCommand.builder().vonalkod("04").mennyiseg(1).build(),
                    TermekMennyisegHozzaadasCommand.builder().vonalkod("05").mennyiseg(1).build(),
                    TermekMennyisegHozzaadasCommand.builder().vonalkod("06").mennyiseg(1).build(),
                    TermekMennyisegHozzaadasCommand.builder().vonalkod("07").mennyiseg(1).build(),
                    TermekMennyisegHozzaadasCommand.builder().vonalkod("08").mennyiseg(1).build(),
                    TermekMennyisegHozzaadasCommand.builder().vonalkod("09").mennyiseg(1).build(),
                    TermekMennyisegHozzaadasCommand.builder().vonalkod("10").mennyiseg(1).build(),
                    TermekMennyisegHozzaadasCommand.builder().vonalkod("11").mennyiseg(1).build(),
                    TermekMennyisegHozzaadasCommand.builder().vonalkod("12").mennyiseg(1).build()
            );
        }

        @BeforeEach
        void setUp() {
            kosarViewDTO = kosarService.kosarViewCreate();
            termekService.saveAll(termekek);

        }

        @AfterEach
        void tearDown() {
            kosarService.deleteKosarById(kosarViewDTO.getKosarId());
            termekService.deleteAll(termekek);
        }

        @Test
        void hozzaad1Elem() {
            TermekMennyisegHozzaadasCommand command = TermekMennyisegHozzaadasCommand.builder()
                    .mennyiseg(1)
                    .vonalkod("01")
                    .build();
            kosarViewDTO = kosarService.addTermekMennyisegCommand(kosarViewDTO.getKosarId(), command);

            assertThat(kosarViewDTO.getTermekMennyisegDtoList())
                    .extracting(TermekMennyisegDto::getNev)
                    .containsExactlyInAnyOrder("Teszt1");
        }

        @Test
        void hozzaadTobbElem() {
            for (TermekMennyisegHozzaadasCommand command : getTermekMennyisegHozzaadasCommandList()) {
                kosarService.addTermekMennyisegCommand(kosarViewDTO.getKosarId(), command);
            }
            kosarViewDTO = kosarService.getKosarDtoById(kosarViewDTO.getKosarId());
            assertThat(kosarViewDTO.getTermekMennyisegDtoList())
                    .extracting(TermekMennyisegDto::getNev)
                    .containsExactlyInAnyOrder("Teszt1",
                            "Teszt2",
                            "Teszt3",
                            "Teszt4",
                            "Teszt5",
                            "Teszt6",
                            "Teszt7",
                            "Teszt8",
                            "Teszt9",
                            "Teszt10",
                            "Teszt11",
                            "Teszt12");
        }

        @Test
        void hozzadEgyElemTobbszor() {
            TermekMennyisegHozzaadasCommand command = TermekMennyisegHozzaadasCommand.builder()
                    .mennyiseg(1)
                    .vonalkod("01")
                    .build();
            kosarService.addTermekMennyisegCommand(kosarViewDTO.getKosarId(), command);
            kosarViewDTO = kosarService.addTermekMennyisegCommand(kosarViewDTO.getKosarId(), command);

            assertEquals(2, kosarViewDTO.getTermekMennyisegDtoList().get(0).getMennyiseg());
        }

        @Test
        void hozzadTobbmintRaktarKeszlet() {

            TermekMennyisegHozzaadasCommand command = TermekMennyisegHozzaadasCommand.builder()
                    .mennyiseg(11)
                    .vonalkod("01")
                    .build();
            String message = null;
            try {
                kosarViewDTO = kosarService.addTermekMennyisegCommand(kosarViewDTO.getKosarId(), command);
            } catch (NincsElegRaktarKeszletException e) {
                message = e.getMessage();
            }

            assertNotNull(message);
        }
    }

}