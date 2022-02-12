package hu.progmatic.kozos.kassza;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BankjegyServiceTest {


    @Autowired
    private BankjegyService bankjegyService;


    private List<Bankjegy> bankjegyek = List.of(
            Bankjegy.builder()
                    .ertek(10000)
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(5000)
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(2000)
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(1000)
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(500)
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(200)
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(100)
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(50)
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(20)
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(10)
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(5)
                    .mennyiseg(3)
                    .build()
    );

    private List<Bankjegy> bankjegyEditor(Integer tizE,
                                           Integer otE,
                                           Integer ketE,
                                           Integer eE,
                                           Integer oSz,
                                           Integer kSz,
                                           Integer Sz,
                                           Integer otven,
                                           Integer husz,
                                           Integer tiz,
                                           Integer ot){
        return List.of(
                Bankjegy.builder()
                        .ertek(10000)
                        .mennyiseg(tizE)
                        .build(),
                Bankjegy.builder()
                        .ertek(5000)
                        .mennyiseg(otE)
                        .build(),
                Bankjegy.builder()
                        .ertek(2000)
                        .mennyiseg(ketE)
                        .build(),
                Bankjegy.builder()
                        .ertek(1000)
                        .mennyiseg(eE)
                        .build(),
                Bankjegy.builder()
                        .ertek(500)
                        .mennyiseg(oSz)
                        .build(),
                Bankjegy.builder()
                        .ertek(200)
                        .mennyiseg(kSz)
                        .build(),
                Bankjegy.builder()
                        .ertek(100)
                        .mennyiseg(Sz)
                        .build(),
                Bankjegy.builder()
                        .ertek(50)
                        .mennyiseg(otven)
                        .build(),
                Bankjegy.builder()
                        .ertek(20)
                        .mennyiseg(husz)
                        .build(),
                Bankjegy.builder()
                        .ertek(10)
                        .mennyiseg(tiz)
                        .build(),
                Bankjegy.builder()
                        .ertek(5)
                        .mennyiseg(ot)
                        .build()
        );
    }

    @BeforeEach
    void setUp() {
        bankjegyService.clear();
    }


    @Test
    @DisplayName("Összes bankjegy")
    void findAll() {
        List<Bankjegy> bankjegyek = bankjegyService.findAll();
        assertThat(bankjegyek).extracting(Bankjegy::getErtek).containsExactlyInAnyOrder(
                20_000,
                10_000,
                5_000,
                2_000,
                1_000,
                500,
                200,
                100,
                50,
                20,
                10,
                5);
    }


    @Test
    @DisplayName("Keresés érték alapján")
    void findByErtek() {
        Bankjegy bankjegy = bankjegyService.findByErtek(1_000);
        assertEquals(1_000, bankjegy.getErtek());

        bankjegy = bankjegyService.findByErtek(10_000);
        assertEquals(10_000, bankjegy.getErtek());

        bankjegy = bankjegyService.findByErtek(20);
        assertEquals(20, bankjegy.getErtek());

        bankjegy = bankjegyService.findByErtek(200);
        assertEquals(200, bankjegy.getErtek());
    }

    @Test
    @DisplayName("Mennyiség szerkesztése")
    void szerkesztes() {
        Bankjegy bankjegy = bankjegyService.findByErtek(1_000);
        bankjegy = bankjegyService.editById(bankjegy.getId(),3 );
        assertEquals(3, bankjegy.getMennyiseg());

        bankjegy = bankjegyService.findByErtek(100);
        bankjegy = bankjegyService.editById(bankjegy.getId(),96 );
        assertEquals(96, bankjegy.getMennyiseg());

        bankjegy = bankjegyService.findByErtek(20_000);
        bankjegy = bankjegyService.editById(bankjegy.getId(),19 );
        assertEquals(19, bankjegy.getMennyiseg());

        bankjegy = bankjegyService.findByErtek(20);
        bankjegy = bankjegyService.editById(bankjegy.getId(),99 );
        assertEquals(99, bankjegy.getMennyiseg());
    }
}