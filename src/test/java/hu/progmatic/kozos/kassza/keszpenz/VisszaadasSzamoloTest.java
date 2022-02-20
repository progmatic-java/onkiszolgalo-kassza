package hu.progmatic.kozos.kassza.keszpenz;

import hu.progmatic.kozos.kassza.Bankjegy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class VisszaadasSzamoloTest {

    private VisszaadasSzamolo visszaadasSzamolo;

    @Test
    void negativKulonbozetTest() {
        List<Bankjegy> banjegyek = List.of(new Bankjegy());

        String message = null;
        try {
            visszaadasSzamolo = new VisszaadasSzamolo(banjegyek, 1);
        } catch (NemLehetNegativErtekException e) {
            message = e.getMessage();
        }
        assertNull(message);

        message = null;
        try {
            visszaadasSzamolo = new VisszaadasSzamolo(banjegyek, -1);
        } catch (NemLehetNegativErtekException e) {
            message = e.getMessage();
        }
        assertNotNull(message);
        assertEquals(message, "Visszajáró csak a végösszegnél nagyobb bedobott érték esetén lehet!");
    }

    @Test
    void nullaKulonbozetTest() {
        List<Bankjegy> banjegyek = List.of(Bankjegy.builder()
                .mennyiseg(1)
                .ertek(10)
                .build());
        visszaadasSzamolo = new VisszaadasSzamolo(banjegyek, 0);
        boolean isVisszaad = visszaadasSzamolo.visszaadasSzamolo();
        assertTrue(isVisszaad);
        assertTrue(visszaadasSzamolo.getVisszajaroLista().isEmpty());

    }

    @Test
    void tudVisszaasniEgyBankjegy() {
        List<Bankjegy> banjegyek = List.of(Bankjegy.builder()
                .mennyiseg(1)
                .ertek(10)
                .build());
        visszaadasSzamolo = new VisszaadasSzamolo(banjegyek, 10);
        boolean isVisszaad = visszaadasSzamolo.visszaadasSzamolo();
        assertTrue(isVisszaad);
        assertThat(visszaadasSzamolo.getVisszajaroLista())
                .extracting(Bankjegy::getErtek)
                .containsExactly(10);
    }

    @Test
    void tudVisszaasniTobbBankjegy() {
        List<Bankjegy> banjegyek =
                List.of(
                        Bankjegy.builder().mennyiseg(1).ertek(5).build(),
                        Bankjegy.builder().mennyiseg(1).ertek(10).build(),
                        Bankjegy.builder().mennyiseg(2).ertek(20).build(),
                        Bankjegy.builder().mennyiseg(2).ertek(500).build(),
                        Bankjegy.builder().mennyiseg(2).ertek(1000).build(),
                        Bankjegy.builder().mennyiseg(1).ertek(2000).build()
                );
        visszaadasSzamolo = new VisszaadasSzamolo(banjegyek, 3555);
        boolean isVisszaad = visszaadasSzamolo.visszaadasSzamolo();
        assertTrue(isVisszaad);
        assertThat(visszaadasSzamolo.getVisszajaroLista())
                .extracting(Bankjegy::getErtek)
                .containsExactlyInAnyOrder(2000, 1000, 500, 20, 10, 5);
        assertEquals(1, visszaadasSzamolo.getVisszajaroLista().get(0).getMennyiseg());
        assertEquals(1, visszaadasSzamolo.getVisszajaroLista().get(1).getMennyiseg());
        assertEquals(1, visszaadasSzamolo.getVisszajaroLista().get(2).getMennyiseg());
        assertEquals(2, visszaadasSzamolo.getVisszajaroLista().get(3).getMennyiseg());
        assertEquals(1, visszaadasSzamolo.getVisszajaroLista().get(4).getMennyiseg());
        assertEquals(1, visszaadasSzamolo.getVisszajaroLista().get(5).getMennyiseg());

        assertThat(visszaadasSzamolo.getBankjegyek())
                .extracting(Bankjegy::getErtek)
                .containsExactlyInAnyOrder(2000, 1000, 500, 20, 10, 5);
        assertEquals(0, visszaadasSzamolo.getBankjegyek().get(0).getMennyiseg());
        assertEquals(1, visszaadasSzamolo.getBankjegyek().get(1).getMennyiseg());
        assertEquals(1, visszaadasSzamolo.getBankjegyek().get(2).getMennyiseg());
        assertEquals(0, visszaadasSzamolo.getBankjegyek().get(3).getMennyiseg());
        assertEquals(0, visszaadasSzamolo.getBankjegyek().get(4).getMennyiseg());
        assertEquals(0, visszaadasSzamolo.getBankjegyek().get(5).getMennyiseg());
    }

    @Test
    void tudVisszaasniTobbBankjegyBonyolult() {
        List<Bankjegy> banjegyek =
                List.of(
                        Bankjegy.builder().mennyiseg(1).ertek(5).build(),
                        Bankjegy.builder().mennyiseg(1).ertek(10).build(),
                        Bankjegy.builder().mennyiseg(2).ertek(20).build(),
                        Bankjegy.builder().mennyiseg(1).ertek(50).build(),
                        Bankjegy.builder().mennyiseg(2).ertek(200).build(),
                        Bankjegy.builder().mennyiseg(1).ertek(500).build(),
                        Bankjegy.builder().mennyiseg(1).ertek(1_000).build(),
                        Bankjegy.builder().mennyiseg(2).ertek(2_000).build(),
                        Bankjegy.builder().mennyiseg(3).ertek(5_000).build(),
                        Bankjegy.builder().mennyiseg(1).ertek(10_000).build(),
                        Bankjegy.builder().mennyiseg(2).ertek(20_000).build()
                );
        visszaadasSzamolo = new VisszaadasSzamolo(banjegyek, 28_735);
        boolean isVisszaad = visszaadasSzamolo.visszaadasSzamolo();
        assertTrue(isVisszaad);
        assertThat(visszaadasSzamolo.getVisszajaroLista())
                .extracting(Bankjegy::getErtek)
                .containsExactlyInAnyOrder(20_000, 10_000, 5000, 2000, 1000, 500, 200, 50, 20, 10, 5);

        assertEquals(1, visszaadasSzamolo.getVisszajaroLista().get(0).getMennyiseg());
        assertEquals(0, visszaadasSzamolo.getVisszajaroLista().get(1).getMennyiseg());
        assertEquals(1, visszaadasSzamolo.getVisszajaroLista().get(2).getMennyiseg());
        assertEquals(1, visszaadasSzamolo.getVisszajaroLista().get(3).getMennyiseg());
        assertEquals(1, visszaadasSzamolo.getVisszajaroLista().get(4).getMennyiseg());
        assertEquals(1, visszaadasSzamolo.getVisszajaroLista().get(5).getMennyiseg());
        assertEquals(1, visszaadasSzamolo.getVisszajaroLista().get(6).getMennyiseg());
        assertEquals(0, visszaadasSzamolo.getVisszajaroLista().get(7).getMennyiseg());
        assertEquals(1, visszaadasSzamolo.getVisszajaroLista().get(8).getMennyiseg());
        assertEquals(1, visszaadasSzamolo.getVisszajaroLista().get(9).getMennyiseg());
        assertEquals(1, visszaadasSzamolo.getVisszajaroLista().get(10).getMennyiseg());

        assertThat(visszaadasSzamolo.getBankjegyek())
                .extracting(Bankjegy::getErtek)
                .containsExactlyInAnyOrder(20_000, 10_000, 5000, 2000, 1000, 500, 200, 50, 20, 10, 5);

        assertEquals(1, visszaadasSzamolo.getBankjegyek().get(0).getMennyiseg());
        assertEquals(1, visszaadasSzamolo.getBankjegyek().get(1).getMennyiseg());
        assertEquals(2, visszaadasSzamolo.getBankjegyek().get(2).getMennyiseg());
        assertEquals(1, visszaadasSzamolo.getBankjegyek().get(3).getMennyiseg());
        assertEquals(0, visszaadasSzamolo.getBankjegyek().get(4).getMennyiseg());
        assertEquals(0, visszaadasSzamolo.getBankjegyek().get(5).getMennyiseg());
        assertEquals(1, visszaadasSzamolo.getBankjegyek().get(6).getMennyiseg());
        assertEquals(1, visszaadasSzamolo.getBankjegyek().get(7).getMennyiseg());
        assertEquals(1, visszaadasSzamolo.getBankjegyek().get(8).getMennyiseg());
        assertEquals(0, visszaadasSzamolo.getBankjegyek().get(9).getMennyiseg());
        assertEquals(0, visszaadasSzamolo.getBankjegyek().get(10).getMennyiseg());
    }


    @Test
    void nemVisszaasniTobbBankjegyBonyolult() {
        List<Bankjegy> banjegyek =
                List.of(
                        Bankjegy.builder().mennyiseg(2).ertek(10).build(),
                        Bankjegy.builder().mennyiseg(1).ertek(50).build(),
                        Bankjegy.builder().mennyiseg(2).ertek(200).build(),
                        Bankjegy.builder().mennyiseg(2).ertek(500).build(),
                        Bankjegy.builder().mennyiseg(1).ertek(1_000).build(),
                        Bankjegy.builder().mennyiseg(2).ertek(2_000).build(),
                        Bankjegy.builder().mennyiseg(1).ertek(5_000).build(),
                        Bankjegy.builder().mennyiseg(4).ertek(10_000).build()
                );
        visszaadasSzamolo = new VisszaadasSzamolo(banjegyek, 36_975);
        boolean isVisszaad = visszaadasSzamolo.visszaadasSzamolo();
        assertFalse(isVisszaad);
        assertThat(visszaadasSzamolo.getVisszajaroLista())
                .extracting(Bankjegy::getErtek)
                .containsExactlyInAnyOrder(10_000, 5000, 2000, 1000, 500, 200, 50, 10);

        assertEquals(3, visszaadasSzamolo.getVisszajaroLista().get(0).getMennyiseg());
        assertEquals(1, visszaadasSzamolo.getVisszajaroLista().get(1).getMennyiseg());
        assertEquals(0, visszaadasSzamolo.getVisszajaroLista().get(2).getMennyiseg());
        assertEquals(1, visszaadasSzamolo.getVisszajaroLista().get(3).getMennyiseg());
        assertEquals(1, visszaadasSzamolo.getVisszajaroLista().get(4).getMennyiseg());
        assertEquals(2, visszaadasSzamolo.getVisszajaroLista().get(5).getMennyiseg());
        assertEquals(1, visszaadasSzamolo.getVisszajaroLista().get(6).getMennyiseg());
        assertEquals(2, visszaadasSzamolo.getVisszajaroLista().get(7).getMennyiseg());

        assertThat(visszaadasSzamolo.getBankjegyek())
                .extracting(Bankjegy::getErtek)
                .containsExactlyInAnyOrder(10_000, 5000, 2000, 1000, 500, 200, 50, 10);

        assertEquals(1, visszaadasSzamolo.getBankjegyek().get(0).getMennyiseg());
        assertEquals(0, visszaadasSzamolo.getBankjegyek().get(1).getMennyiseg());
        assertEquals(2, visszaadasSzamolo.getBankjegyek().get(2).getMennyiseg());
        assertEquals(0, visszaadasSzamolo.getBankjegyek().get(3).getMennyiseg());
        assertEquals(1, visszaadasSzamolo.getBankjegyek().get(4).getMennyiseg());
        assertEquals(0, visszaadasSzamolo.getBankjegyek().get(5).getMennyiseg());
        assertEquals(0, visszaadasSzamolo.getBankjegyek().get(6).getMennyiseg());
        assertEquals(0, visszaadasSzamolo.getBankjegyek().get(7).getMennyiseg());
    }

}