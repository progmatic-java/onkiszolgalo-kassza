package hu.progmatic.kozos.kassza.keszpenz;

import hu.progmatic.kozos.kassza.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class KeszpenzVisszaadasTest {

    @Nested
    @DisplayName("A végösszegnél kevesebb, illetve azzal megegyezö összeg bedobása")
    class VisszajaroKisebbTest {


       /* public KeszpenzDto visszajaro(KeszpenzDto keszpenzDto) {
            Kosar kosar = kosarService.getById(keszpenzDto.getKosarId());
            List<Bankjegy> bankjegyek = bankjegyService.findAll();
            KeszpenzVisszaadas keszpenzVisszaadas = new KeszpenzVisszaadas(kosar, bankjegyek);
            if (keszpenzDto.getBedobottCimlet().equals(0)) {
                keszpenzDto.setEnabledBankjegyek(keszpenzVisszaadas.enabledBankjegyekMeghatarozasa());
                initKeszpenzDto(keszpenzDto, keszpenzVisszaadas.getKosarVegosszegRoundFive());
            }
            keszpenzVisszaadas.addBedobottCimlet(bankjegyService.findByErtek(keszpenzDto.getBedobottCimlet()));
            keszpenzVisszaadas.szamolas();
            keszpenzDto.setVegosszeg(keszpenzVisszaadas.getKosarVegosszegRoundFive());
            keszpenzDto.setMaradek(keszpenzVisszaadas.getMaradek());
            keszpenzDto.setEnabledBankjegyek(keszpenzVisszaadas.enabledBankjegyekMeghatarozasa());
            keszpenzDto.setVisszajaro(
                    visszajaroListToStr(
                            keszpenzVisszaadas.getVisszajaroBankjegyek()));
            return keszpenzDto;
        }*/

        private Kosar kosar;

        @BeforeEach
        void setUp() {
            kosar = new Kosar();
        }

        @Test
        void krekitesítestMaradek() {
            kosar.getTermekMennyisegek().add(termekMennyisegBuilder(1, 1_322));
            List<Bankjegy> bankjegyek =
                    List.of(
                            Bankjegy.builder().mennyiseg(0).ertek(500).build()
                    );
            KeszpenzVisszaadas keszpenzVisszaadas = new KeszpenzVisszaadas(kosar, bankjegyek);
            assertEquals(1_320, keszpenzVisszaadas.getKosarVegosszegRoundFive());
        }

        @Test
        void megNemKellVisszaadni() {
            kosar.getTermekMennyisegek().add(termekMennyisegBuilder(1, 1_327));
            Bankjegy bankjegy = Bankjegy.builder()
                    .mennyiseg(1)
                    .ertek(20)
                    .build();
            List<Bankjegy> bankjegyek = new ArrayList<>();
            bankjegyek.add(bankjegy);
            KeszpenzVisszaadas keszpenzVisszaadas = new KeszpenzVisszaadas(kosar, bankjegyek);
            keszpenzVisszaadas.addBedobottCimlet(Bankjegy.builder()
                    .mennyiseg(1)
                    .ertek(20)
                    .build());
            keszpenzVisszaadas.szamolas();

            assertEquals(1305, keszpenzVisszaadas.getMaradek());
            assertTrue(keszpenzVisszaadas.getVisszajaroBankjegyek().isEmpty());
            assertThat(keszpenzVisszaadas.getBankjegyek()).extracting(Bankjegy::getErtek).containsExactlyInAnyOrder(20);
            assertThat(keszpenzVisszaadas.getBankjegyek()).extracting(Bankjegy::getMennyiseg).containsExactlyInAnyOrder(2);
        }

        private TermekMennyiseg termekMennyisegBuilder(Integer mennyiseg, Integer ar) {
            return TermekMennyiseg.builder()
                    .mennyiseg(mennyiseg)
                    .kosar(kosar)
                    .termek(Termek.builder()
                            .ar(ar)
                            .build())
                    .build();
        }

        @Test
        void nemEngedelyezettBankjegy() {
            kosar.getTermekMennyisegek().add(termekMennyisegBuilder(1, 1_500));
            List<Bankjegy> bankjegyek =
                    List.of(
                            Bankjegy.builder().mennyiseg(0).ertek(500).build(),
                            Bankjegy.builder().mennyiseg(0).ertek(1000).build(),
                            Bankjegy.builder().mennyiseg(0).ertek(5000).build()
                    );

            KeszpenzVisszaadas keszpenzVisszaadas = new KeszpenzVisszaadas(kosar, bankjegyek);
            keszpenzVisszaadas.addBedobottCimlet(Bankjegy.builder()
                    .mennyiseg(1)
                    .ertek(5000)
                    .build());
            String message = null;
            try {
                keszpenzVisszaadas.szamolas();
            } catch (NemEngedelyezettBankjegyException e) {
                message = e.getMessage();
            }

            assertNotNull(message);
        }

        @Test
        void enabledListTest() {
            kosar.getTermekMennyisegek().add(termekMennyisegBuilder(1, 1_435));
            List<Bankjegy> bankjegyek =
                    List.of(
                            Bankjegy.builder().mennyiseg(1).ertek(5).build(),
                            Bankjegy.builder().mennyiseg(1).ertek(10).build(),
                            Bankjegy.builder().mennyiseg(0).ertek(20).build(),
                            Bankjegy.builder().mennyiseg(1).ertek(50).build(),
                            Bankjegy.builder().mennyiseg(0).ertek(100).build(),
                            Bankjegy.builder().mennyiseg(0).ertek(200).build(),
                            Bankjegy.builder().mennyiseg(1).ertek(500).build(),
                            Bankjegy.builder().mennyiseg(0).ertek(1_000).build(),
                            Bankjegy.builder().mennyiseg(1).ertek(2_000).build(),
                            Bankjegy.builder().mennyiseg(1).ertek(5_000).build(),
                            Bankjegy.builder().mennyiseg(1).ertek(10_000).build(),
                            Bankjegy.builder().mennyiseg(1).ertek(20_000).build()
                    );
            KeszpenzVisszaadas keszpenzVisszaadas = new KeszpenzVisszaadas(kosar, bankjegyek);
            List<EnabledBankjegyDto> enabledBankjegyDtoList = keszpenzVisszaadas.enabledBankjegyekMeghatarozasa();

            EnabledBankjegyDto enabledBankjegyDto = enabledBankjegyDtoList.stream().filter(bj -> bj.getBankjegyErtek().equals(1_000)).findAny().orElseThrow();
            assertTrue(enabledBankjegyDto.isEngedelyezve());
            enabledBankjegyDto = enabledBankjegyDtoList.stream().filter(bj -> bj.getBankjegyErtek().equals(2_000)).findAny().orElseThrow();
            assertTrue(enabledBankjegyDto.isEngedelyezve());
            enabledBankjegyDto = enabledBankjegyDtoList.stream().filter(bj -> bj.getBankjegyErtek().equals(5_000)).findAny().orElseThrow();
            assertFalse(enabledBankjegyDto.isEngedelyezve());
            enabledBankjegyDto = enabledBankjegyDtoList.stream().filter(bj -> bj.getBankjegyErtek().equals(10_000)).findAny().orElseThrow();
            assertFalse(enabledBankjegyDto.isEngedelyezve());
            enabledBankjegyDto = enabledBankjegyDtoList.stream().filter(bj -> bj.getBankjegyErtek().equals(20_000)).findAny().orElseThrow();
            assertFalse(enabledBankjegyDto.isEngedelyezve());
        }

        @Test
        void allDisabledListTest() {
            kosar.getTermekMennyisegek().add(termekMennyisegBuilder(1, 1_435));
            List<Bankjegy> bankjegyek =
                    List.of(
                            Bankjegy.builder().mennyiseg(0).ertek(5).build(),
                            Bankjegy.builder().mennyiseg(0).ertek(10).build(),
                            Bankjegy.builder().mennyiseg(0).ertek(20).build(),
                            Bankjegy.builder().mennyiseg(0).ertek(50).build(),
                            Bankjegy.builder().mennyiseg(0).ertek(100).build(),
                            Bankjegy.builder().mennyiseg(0).ertek(200).build(),
                            Bankjegy.builder().mennyiseg(0).ertek(500).build(),
                            Bankjegy.builder().mennyiseg(0).ertek(1_000).build(),
                            Bankjegy.builder().mennyiseg(0).ertek(2_000).build(),
                            Bankjegy.builder().mennyiseg(0).ertek(5_000).build(),
                            Bankjegy.builder().mennyiseg(0).ertek(10_000).build(),
                            Bankjegy.builder().mennyiseg(0).ertek(20_000).build()
                    );
            KeszpenzVisszaadas keszpenzVisszaadas = new KeszpenzVisszaadas(kosar, bankjegyek);
            List<EnabledBankjegyDto> enabledBankjegyDtoList = keszpenzVisszaadas.enabledBankjegyekMeghatarozasa();
            keszpenzVisszaadas.addBedobottCimlet(Bankjegy.builder().mennyiseg(0).ertek(1_000).build());
            enabledBankjegyDtoList = keszpenzVisszaadas.enabledBankjegyekMeghatarozasa();
            EnabledBankjegyDto enabledBankjegyDto = enabledBankjegyDtoList.stream().filter(bj -> bj.getBankjegyErtek().equals(1_000)).findAny().orElseThrow();
            assertFalse(enabledBankjegyDto.isEngedelyezve());

            keszpenzVisszaadas.addBedobottCimlet(Bankjegy.builder().mennyiseg(0).ertek(200).build());
            keszpenzVisszaadas.addBedobottCimlet(Bankjegy.builder().mennyiseg(0).ertek(200).build());
            enabledBankjegyDtoList = keszpenzVisszaadas.enabledBankjegyekMeghatarozasa();
            enabledBankjegyDto = enabledBankjegyDtoList.stream().filter(bj -> bj.getBankjegyErtek().equals(200)).findAny().orElseThrow();
            assertFalse(enabledBankjegyDto.isEngedelyezve());

            keszpenzVisszaadas.addBedobottCimlet(Bankjegy.builder().mennyiseg(0).ertek(20).build());
            enabledBankjegyDtoList = keszpenzVisszaadas.enabledBankjegyekMeghatarozasa();
            enabledBankjegyDto = enabledBankjegyDtoList.stream().filter(bj -> bj.getBankjegyErtek().equals(20)).findAny().orElseThrow();
            assertFalse(enabledBankjegyDto.isEngedelyezve());

            keszpenzVisszaadas.addBedobottCimlet(Bankjegy.builder().mennyiseg(0).ertek(10).build());
            enabledBankjegyDtoList = keszpenzVisszaadas.enabledBankjegyekMeghatarozasa();
            enabledBankjegyDto = enabledBankjegyDtoList.stream().filter(bj -> bj.getBankjegyErtek().equals(10)).findAny().orElseThrow();
            assertFalse(enabledBankjegyDto.isEngedelyezve());

            keszpenzVisszaadas.addBedobottCimlet(Bankjegy.builder().mennyiseg(0).ertek(5).build());
            enabledBankjegyDtoList = keszpenzVisszaadas.enabledBankjegyekMeghatarozasa();

            for (EnabledBankjegyDto enabledBDto : enabledBankjegyDtoList) {
                assertFalse(enabledBDto.isEngedelyezve());
            }
        }

        @Test
        void vanVisszajaro() {
            kosar.getTermekMennyisegek().add(termekMennyisegBuilder(1, 1_447));
            List<Bankjegy> bankjegyek =
                    List.of(
                            Bankjegy.builder().mennyiseg(1).ertek(5).build(),
                            Bankjegy.builder().mennyiseg(1).ertek(10).build(),
                            Bankjegy.builder().mennyiseg(2).ertek(20).build(),
                            Bankjegy.builder().mennyiseg(2).ertek(500).build(),
                            Bankjegy.builder().mennyiseg(2).ertek(1000).build(),
                            Bankjegy.builder().mennyiseg(1).ertek(2000).build(),
                            Bankjegy.builder().mennyiseg(0).ertek(5000).build()
                    );

            KeszpenzVisszaadas keszpenzVisszaadas = new KeszpenzVisszaadas(kosar, bankjegyek);
            keszpenzVisszaadas.addBedobottCimlet(Bankjegy.builder()
                    .ertek(5000)
                    .mennyiseg(1)
                    .build());
            keszpenzVisszaadas.szamolas();
            List<Bankjegy> visszajaro = keszpenzVisszaadas.getVisszajaroBankjegyek();
            List<Bankjegy> bentMaradtBankjegyek = keszpenzVisszaadas.getBankjegyek();

            assertEquals(5000, visszajaro.get(0).getErtek());
            assertEquals(0, visszajaro.get(0).getMennyiseg());

            assertEquals(2000, visszajaro.get(1).getErtek());
            assertEquals(1, visszajaro.get(1).getMennyiseg());

            assertEquals(1000, visszajaro.get(2).getErtek());
            assertEquals(1, visszajaro.get(2).getMennyiseg());

            assertEquals(500, visszajaro.get(3).getErtek());
            assertEquals(1, visszajaro.get(3).getMennyiseg());

            assertEquals(20, visszajaro.get(4).getErtek());
            assertEquals(2, visszajaro.get(4).getMennyiseg());

            assertEquals(10, visszajaro.get(5).getErtek());
            assertEquals(1, visszajaro.get(5).getMennyiseg());

            assertEquals(5, visszajaro.get(6).getErtek());
            assertEquals(1, visszajaro.get(6).getMennyiseg());

            assertEquals(5000, bentMaradtBankjegyek.get(6).getErtek());
            assertEquals(1, bentMaradtBankjegyek.get(6).getMennyiseg());

            assertEquals(2000, bentMaradtBankjegyek.get(5).getErtek());
            assertEquals(0, bentMaradtBankjegyek.get(5).getMennyiseg());

            assertEquals(1000, bentMaradtBankjegyek.get(4).getErtek());
            assertEquals(1, bentMaradtBankjegyek.get(4).getMennyiseg());

            assertEquals(500, bentMaradtBankjegyek.get(3).getErtek());
            assertEquals(1, bentMaradtBankjegyek.get(3).getMennyiseg());

            assertEquals(20, bentMaradtBankjegyek.get(2).getErtek());
            assertEquals(0, bentMaradtBankjegyek.get(2).getMennyiseg());

            assertEquals(10, bentMaradtBankjegyek.get(1).getErtek());
            assertEquals(0, bentMaradtBankjegyek.get(1).getMennyiseg());

            assertEquals(5, bentMaradtBankjegyek.get(0).getErtek());
            assertEquals(0, bentMaradtBankjegyek.get(0).getMennyiseg());
        }

        @Test
        void visszavonasTest() {
            kosar.getTermekMennyisegek().add(termekMennyisegBuilder(1, 5000));
            List<Bankjegy> bankjegyek =
                    List.of(
                            Bankjegy.builder().mennyiseg(1).ertek(5).build(),
                            Bankjegy.builder().mennyiseg(1).ertek(10).build(),
                            Bankjegy.builder().mennyiseg(2).ertek(20).build(),
                            Bankjegy.builder().mennyiseg(2).ertek(500).build(),
                            Bankjegy.builder().mennyiseg(2).ertek(1000).build(),
                            Bankjegy.builder().mennyiseg(1).ertek(2000).build(),
                            Bankjegy.builder().mennyiseg(0).ertek(5000).build()
                    );

            KeszpenzVisszaadas keszpenzVisszaadas = new KeszpenzVisszaadas(kosar, bankjegyek);
            keszpenzVisszaadas.addBedobottCimlet(Bankjegy.builder().ertek(500).mennyiseg(1).build());
            keszpenzVisszaadas.addBedobottCimlet(Bankjegy.builder().ertek(500).mennyiseg(1).build());
            keszpenzVisszaadas.addBedobottCimlet(Bankjegy.builder().ertek(2000).mennyiseg(1).build());
            keszpenzVisszaadas.addBedobottCimlet(Bankjegy.builder().ertek(20).mennyiseg(1).build());
            keszpenzVisszaadas.addBedobottCimlet(Bankjegy.builder().ertek(1000).mennyiseg(1).build());
            //keszpenzVisszaadas.szamolas();
            keszpenzVisszaadas.fizetesVisszavonas();
            List<Bankjegy> visszajaro = keszpenzVisszaadas.getVisszajaroBankjegyek();
            List<Bankjegy> bentMaradtBankjegyek = keszpenzVisszaadas.getBankjegyek();

            assertEquals(5000, visszajaro.get(6).getErtek());
            assertEquals(0, visszajaro.get(6).getMennyiseg());

            assertEquals(2000, visszajaro.get(5).getErtek());
            assertEquals(1, visszajaro.get(5).getMennyiseg());

            assertEquals(1000, visszajaro.get(4).getErtek());
            assertEquals(1, visszajaro.get(4).getMennyiseg());

            assertEquals(500, visszajaro.get(3).getErtek());
            assertEquals(2, visszajaro.get(3).getMennyiseg());

            assertEquals(20, visszajaro.get(2).getErtek());
            assertEquals(1, visszajaro.get(2).getMennyiseg());

            assertEquals(10, visszajaro.get(1).getErtek());
            assertEquals(0, visszajaro.get(1).getMennyiseg());

            assertEquals(5, visszajaro.get(0).getErtek());
            assertEquals(0, visszajaro.get(0).getMennyiseg());

            assertEquals(5000, bentMaradtBankjegyek.get(6).getErtek());
            assertEquals(0, bentMaradtBankjegyek.get(6).getMennyiseg());

            assertEquals(2000, bentMaradtBankjegyek.get(5).getErtek());
            assertEquals(1, bentMaradtBankjegyek.get(5).getMennyiseg());

            assertEquals(1000, bentMaradtBankjegyek.get(4).getErtek());
            assertEquals(2, bentMaradtBankjegyek.get(4).getMennyiseg());

            assertEquals(500, bentMaradtBankjegyek.get(3).getErtek());
            assertEquals(2, bentMaradtBankjegyek.get(3).getMennyiseg());

            assertEquals(20, bentMaradtBankjegyek.get(2).getErtek());
            assertEquals(2, bentMaradtBankjegyek.get(2).getMennyiseg());

            assertEquals(10, bentMaradtBankjegyek.get(1).getErtek());
            assertEquals(1, bentMaradtBankjegyek.get(1).getMennyiseg());

            assertEquals(5, bentMaradtBankjegyek.get(0).getErtek());
            assertEquals(1, bentMaradtBankjegyek.get(0).getMennyiseg());
        }

        @Test
        void enabledTovabbiTest() {
            kosar.getTermekMennyisegek().add(termekMennyisegBuilder(1, 200));
            List<Bankjegy> bankjegyek =
                    List.of(
                            Bankjegy.builder().mennyiseg(3).ertek(5).build(),
                            Bankjegy.builder().mennyiseg(3).ertek(10).build(),
                            Bankjegy.builder().mennyiseg(3).ertek(20).build(),
                            Bankjegy.builder().mennyiseg(3).ertek(50).build(),
                            Bankjegy.builder().mennyiseg(3).ertek(100).build(),
                            Bankjegy.builder().mennyiseg(3).ertek(200).build(),
                            Bankjegy.builder().mennyiseg(3).ertek(500).build(),
                            Bankjegy.builder().mennyiseg(3).ertek(1_000).build(),
                            Bankjegy.builder().mennyiseg(3).ertek(2_000).build(),
                            Bankjegy.builder().mennyiseg(3).ertek(5_000).build(),
                            Bankjegy.builder().mennyiseg(3).ertek(10_000).build(),
                            Bankjegy.builder().mennyiseg(0).ertek(20_000).build()
                    );
            KeszpenzVisszaadas keszpenzVisszaadas = new KeszpenzVisszaadas(kosar, bankjegyek);
            List<EnabledBankjegyDto> enabledBankjegyDtoList = keszpenzVisszaadas.enabledBankjegyekMeghatarozasa();
            System.out.println("asd");
        }
    }
}
