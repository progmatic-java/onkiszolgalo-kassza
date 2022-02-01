package hu.progmatic.kozos.kassza;

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

    @Test
    void kosarLetrehozas() {
        KosarViewDTO kosarViewDTO = kosarService.kosarViewCreate();
        assertNotNull(kosarViewDTO.getKosarId());
    }

    @Nested
    class HozzaadTest {

        KosarViewDTO kosarViewDTO;

        private List<TermekMennyisegHozzaadasCommand> getTermekMennyisegHozzaadasCommandList(KosarViewDTO kosar) {
            return List.of(
                    TermekMennyisegHozzaadasCommand.builder().kosarId(kosar.getKosarId()).vonalkod("12345").mennyiseg(10).build(),
                    TermekMennyisegHozzaadasCommand.builder().kosarId(kosar.getKosarId()).vonalkod("12335").mennyiseg(10).build(),
                    TermekMennyisegHozzaadasCommand.builder().kosarId(kosar.getKosarId()).vonalkod("1232345").mennyiseg(10).build(),
                    TermekMennyisegHozzaadasCommand.builder().kosarId(kosar.getKosarId()).vonalkod("12344345").mennyiseg(10).build(),
                    TermekMennyisegHozzaadasCommand.builder().kosarId(kosar.getKosarId()).vonalkod("12134235").mennyiseg(10).build(),
                    TermekMennyisegHozzaadasCommand.builder().kosarId(kosar.getKosarId()).vonalkod("23212345").mennyiseg(10).build(),
                    TermekMennyisegHozzaadasCommand.builder().kosarId(kosar.getKosarId()).vonalkod("16752345").mennyiseg(10).build(),
                    TermekMennyisegHozzaadasCommand.builder().kosarId(kosar.getKosarId()).vonalkod("123226345").mennyiseg(10).build(),
                    TermekMennyisegHozzaadasCommand.builder().kosarId(kosar.getKosarId()).vonalkod("121134354345").mennyiseg(10).build(),
                    TermekMennyisegHozzaadasCommand.builder().kosarId(kosar.getKosarId()).vonalkod("113344512345").mennyiseg(10).build(),
                    TermekMennyisegHozzaadasCommand.builder().kosarId(kosar.getKosarId()).vonalkod("1234133425").mennyiseg(10).build(),
                    TermekMennyisegHozzaadasCommand.builder().kosarId(kosar.getKosarId()).vonalkod("1221421345").mennyiseg(10).build()
            );
        }

        @BeforeEach
        void setUp() {
            kosarViewDTO = kosarService.kosarViewCreate();
        }

        @AfterEach
        void tearDown() {
            kosarService.deleteKosarById(kosarViewDTO.getKosarId());
        }

        @Test
        void hozzaad1Elem() {
            TermekMennyisegHozzaadasCommand command = TermekMennyisegHozzaadasCommand.builder()
                    .kosarId(kosarViewDTO.getKosarId())
                    .mennyiseg(5)
                    .vonalkod("12345")
                    .build();
            kosarViewDTO = kosarService.addTermekMennyisegCommand(command);

            assertThat(kosarViewDTO.getTermekMennyisegDtoList())
                    .extracting(termekMennyisegDto -> termekMennyisegDto.getNev())
                    .containsExactlyInAnyOrder("víz");
        }

        @Test
        void hozzaadTobbElem() {
            for (TermekMennyisegHozzaadasCommand command : getTermekMennyisegHozzaadasCommandList(kosarViewDTO)) {
                kosarService.addTermekMennyisegCommand(command);
            }
            kosarViewDTO = kosarService.getKosarDtoById(kosarViewDTO.getKosarId());
            assertThat(kosarViewDTO.getTermekMennyisegDtoList())
                    .extracting(termekMennyisegDto -> termekMennyisegDto.getNev())
                    .containsExactlyInAnyOrder("víz",
                            "kóla",
                            "sör",
                            "kenyér",
                            "Joghurt",
                            "fanta",
                            "zsemle",
                            "bor",
                            "vodka",
                            "narancs",
                            "tüske",
                            "cigaretta");
        }

    }


}