package hu.progmatic.kozos.kassza;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KeszpenzServiceTest {

    @Autowired
    private KeszpenzService keszpenzService;

    @Autowired
    private KosarService kosarService;

    @Autowired
    TermekService termekService;

    @Autowired
    private BankjegyService bankjegyService;


    @Nested
    @DisplayName("A végösszegnél kevesebb, illetve azzal megegyezö összeg bedobása")
    class VisszajaroKisebbTest {

        private KosarViewDTO kosarViewDTO;
        private Integer termekId;

        @BeforeEach
        void setUp() {
            Termek termek = termekService.create(Termek.builder()
                    .megnevezes("TesztItem1")
                    .mennyiseg(1)
                    .vonalkod("99999990")
                    .ar(13_287)
                    .build());
            termekId = termek.getId();
            kosarViewDTO = kosarService.addTermekMennyisegCommand(
                    kosarService.kosarViewCreate().getKosarId(),
                    TermekMennyisegHozzaadasCommand.builder()
                            .vonalkod(termek.getVonalkod())
                            .mennyiseg(1)
                            .build());
        }

        @AfterEach
        void tearDown() {
            kosarService.deleteKosarById(kosarViewDTO.getKosarId());
            termekService.deleteById(termekId);
        }

        @Test
        @DisplayName("A felület beöltéséhez szükséges KeszpenzDto")
        void kezdetiKeszpenzDto() {

            KeszpenzDto keszpenzDto = keszpenzService.visszajaro(KeszpenzDto.builder()
                    .kosarId(kosarViewDTO.getKosarId())
                    .build());


            assertEquals(13_285, keszpenzDto.getVegosszeg());
            assertEquals(13_285, keszpenzDto.getMaradek());
            assertNull(keszpenzDto.getVisszajaro());
            assertNull(keszpenzDto.getBedobottCimlet());
            assertFalse(keszpenzDto.isNemTudVisszaadni());
        }

        @Test
        @DisplayName("Bedobott kisebb címlet mint a végösszeg")
        void bedobottKisCimlet() {
            KeszpenzDto keszpenzDto = KeszpenzDto.builder()
                    .kosarId(kosarViewDTO.getKosarId())
                    .bedobottCimlet(10_000)
                    .build();
            keszpenzDto = keszpenzService.visszajaro(keszpenzDto);

            assertEquals(13_285, keszpenzDto.getVegosszeg());
            assertEquals(3_285, keszpenzDto.getMaradek());
            assertNull(keszpenzDto.getVisszajaro());
            assertFalse(keszpenzDto.isNemTudVisszaadni());

        }

        @Test
        @DisplayName("Bedobott több kisebb címlet mint a végösszeg")
        void bedobottTobbKisCimlet() {
            KeszpenzDto keszpenzDto = KeszpenzDto.builder()
                    .kosarId(kosarViewDTO.getKosarId())
                    .bedobottCimlet(10_000)
                    .build();
            keszpenzDto = keszpenzService.visszajaro(keszpenzDto);
            keszpenzDto.setBedobottCimlet(200);
            keszpenzDto = keszpenzService.visszajaro(keszpenzDto);
            keszpenzDto.setBedobottCimlet(2_000);
            keszpenzDto = keszpenzService.visszajaro(keszpenzDto);
            keszpenzDto.setBedobottCimlet(5);
            keszpenzDto = keszpenzService.visszajaro(keszpenzDto);


            assertEquals(13_285, keszpenzDto.getVegosszeg());
            assertEquals(1080, keszpenzDto.getMaradek());
            assertNull(keszpenzDto.getVisszajaro());
            assertFalse(keszpenzDto.isNemTudVisszaadni());

        }

        @Test
        @DisplayName("Bedobott több kisebb címlet megegyezik a végösszeg")
        void bedobottTobbKisCimletVegosszeg() {
            KeszpenzDto keszpenzDto = KeszpenzDto.builder()
                    .kosarId(kosarViewDTO.getKosarId())
                    .bedobottCimlet(10_000)
                    .build();
            keszpenzDto = keszpenzService.visszajaro(keszpenzDto);
            keszpenzDto.setBedobottCimlet(1_000);
            keszpenzDto = keszpenzService.visszajaro(keszpenzDto);
            keszpenzDto.setBedobottCimlet(2_000);
            keszpenzDto = keszpenzService.visszajaro(keszpenzDto);
            keszpenzDto.setBedobottCimlet(200);
            keszpenzDto = keszpenzService.visszajaro(keszpenzDto);
            keszpenzDto.setBedobottCimlet(50);
            keszpenzDto = keszpenzService.visszajaro(keszpenzDto);
            keszpenzDto.setBedobottCimlet(20);
            keszpenzDto = keszpenzService.visszajaro(keszpenzDto);
            keszpenzDto.setBedobottCimlet(10);
            keszpenzDto = keszpenzService.visszajaro(keszpenzDto);
            keszpenzDto.setBedobottCimlet(5);
            keszpenzDto = keszpenzService.visszajaro(keszpenzDto);

            assertEquals(13_285, keszpenzDto.getVegosszeg());
            assertEquals(0, keszpenzDto.getMaradek());
            assertNotNull(keszpenzDto.getVisszajaro());
            assertFalse(keszpenzDto.isNemTudVisszaadni());
        }
    }

    @Nested
    @DisplayName("A végösszegnél nagyobb, címlet bedobása")
    class VisszajaroNagyobbTest {

        private KosarViewDTO kosarViewDTO;
        private Integer termekId;

        @BeforeEach
        void setUp() {
            Termek termek = termekService.create(Termek.builder()
                    .megnevezes("TesztItem1")
                    .mennyiseg(1)
                    .vonalkod("99999990")
                    .ar(1_637)
                    .build());
            termekId = termek.getId();
            kosarViewDTO = kosarService.addTermekMennyisegCommand(
                    kosarService.kosarViewCreate().getKosarId(),
                    TermekMennyisegHozzaadasCommand.builder()
                            .vonalkod(termek.getVonalkod())
                            .mennyiseg(1)
                            .build());
            bankjegyService.clear();
        }

        @AfterEach
        void tearDown() {
            kosarService.deleteKosarById(kosarViewDTO.getKosarId());
            termekService.deleteById(termekId);
        }

        @Test
        void cimlet1() {
            cimletHozzaad(100, 1);
            cimletHozzaad(200, 1);
            cimletHozzaad(50, 1);
            cimletHozzaad(10, 1);
            cimletHozzaad(5, 1);
            KeszpenzDto keszpenzDto = KeszpenzDto.builder()
                    .kosarId(kosarViewDTO.getKosarId())
                    .bedobottCimlet(2_000)
                    .build();
            keszpenzDto = keszpenzService.visszajaro(keszpenzDto);

            assertEquals(1_635, keszpenzDto.getVegosszeg());
            assertNull(keszpenzDto.getMaradek());
            assertNotNull(keszpenzDto.getVisszajaro());
            assertFalse(keszpenzDto.isNemTudVisszaadni());

        }

        @Disabled
        // legyen még ezelött pár teszt
        @Test
        void nemTudVisszadni() {
            cimletHozzaad(100, 1);
            cimletHozzaad(200, 1);
            cimletHozzaad(50, 1);
            cimletHozzaad(5, 1);
            KeszpenzDto keszpenzDto = KeszpenzDto.builder()
                    .kosarId(kosarViewDTO.getKosarId())
                    .bedobottCimlet(2_000)
                    .build();
            keszpenzDto = keszpenzService.visszajaro(keszpenzDto);

            assertEquals(1_635, keszpenzDto.getVegosszeg());
            assertEquals(1_635, keszpenzDto.getMaradek());
            assertNotNull(keszpenzDto.getVisszajaro());
            assertTrue(keszpenzDto.isNemTudVisszaadni());
            System.out.println(keszpenzDto.getVisszajaro());

        }

        private void cimletHozzaad(Integer ertek, Integer mennyiseg) {
            Bankjegy bankjegy = bankjegyService.findByErtek(ertek);
            bankjegyService.editById(bankjegy.getId(), mennyiseg);
        }
    }
}