package hu.progmatic.kozos.kassza.keszpenz;

import hu.progmatic.kozos.kassza.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class VisszajaroElokeszitoTest {

    private Kosar kosar;
    private VisszajaroElokeszito visszajaroElokeszito;

    @BeforeEach
    void beforeEach() {
        kosar = new Kosar();
        visszajaroElokeszito = new VisszajaroElokeszito(kosar);
    }

    @Test
    void krekitesítestMaradek1() {
        kosar.getTermekMennyisegek().add(termekMennyisegBuilder(1, 1_321));
        assertEquals(1_320, visszajaroElokeszito.getKosarVegosszegRoundFive());
    }

    @Test
    void krekitesítestMaradek2() {
        kosar.getTermekMennyisegek().add(termekMennyisegBuilder(1, 1_522));
        assertEquals(1_520, visszajaroElokeszito.getKosarVegosszegRoundFive());
    }

    @Test
    void krekitesítestMaradek3() {
        kosar.getTermekMennyisegek().add(termekMennyisegBuilder(1, 1_343));
        assertEquals(1_345, visszajaroElokeszito.getKosarVegosszegRoundFive());
    }

    @Test
    void krekitesítestMaradek4() {
        kosar.getTermekMennyisegek().add(termekMennyisegBuilder(1, 11_964));
        assertEquals(11_965, visszajaroElokeszito.getKosarVegosszegRoundFive());
    }

    @Test
    void krekitesítestMaradek0() {
        kosar.getTermekMennyisegek().add(termekMennyisegBuilder(1, 125));
        assertEquals(125, visszajaroElokeszito.getKosarVegosszegRoundFive());
    }


    @Nested
    class BedobottBankjegyekTest {

        @BeforeEach
        void setUp() {
            kosar.getTermekMennyisegek().add(termekMennyisegBuilder(1, 1_138));
            visszajaroElokeszito.addBedobottCimletToKosar(Bankjegy.builder()
                    .ertek(1_125)
                    .mennyiseg(1)
                    .build());
            visszajaroElokeszito.addBedobottCimletToKosar(Bankjegy.builder()
                    .ertek(4_460)
                    .mennyiseg(1)
                    .build());
        }

        @Test
        void osszegBedobvaTest() {
            assertEquals(1_125 + 4_460, visszajaroElokeszito.osszegBedobva());
        }

        @Test
        void kulonbozet() {
            assertEquals(1_125 + 4_460 - 1_140, visszajaroElokeszito.getKulonbozet());
        }

        @Test
        void bedobottBankjgyek() {
            assertThat(visszajaroElokeszito.getBedobottBankjegyek())
                    .extracting(bedobottBankjegy -> bedobottBankjegy.getBankjegy().getErtek())
                    .containsExactlyInAnyOrder(1_125, 4_460);
        }
    }


    @Nested
    class BedobottBankjegyekTovabbiTest {

        @BeforeEach
        void setUp() {
            kosar.getTermekMennyisegek().add(termekMennyisegBuilder(1, 1_138));
            visszajaroElokeszito.addBedobottCimletToKosar(Bankjegy.builder()
                    .ertek(15)
                    .mennyiseg(1)
                    .build());
            visszajaroElokeszito.addBedobottCimletToKosar(Bankjegy.builder()
                    .ertek(15)
                    .mennyiseg(1)
                    .build());
        }

        @Test
        void osszegBedobvaTest() {
            assertEquals(30, visszajaroElokeszito.osszegBedobva());
        }

        @Test
        void kulonbozet() {
            assertEquals(30 - 1_140, visszajaroElokeszito.getKulonbozet());
        }

        @Test
        void bedobottBankjgyek() {
            assertThat(visszajaroElokeszito.getBedobottBankjegyek())
                    .extracting(bedobottBankjegy -> bedobottBankjegy.getBankjegy().getErtek())
                    .containsExactlyInAnyOrder(15);
            assertThat(visszajaroElokeszito.getBedobottBankjegyek())
                    .extracting(BedobottBankjegy::getBedobottMenyiseg)
                    .containsExactlyInAnyOrder(2);
        }
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


    private BedobottBankjegy bedobottBankjegyBuilder(Integer darab, Integer ertek) {
        return BedobottBankjegy.builder()
                .kosar(kosar)
                .bedobottMenyiseg(darab)
                .bankjegy(Bankjegy.builder()
                        .ertek(ertek)
                        .build())
                .build();
    }
}