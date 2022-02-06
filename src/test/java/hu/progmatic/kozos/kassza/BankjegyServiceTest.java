package hu.progmatic.kozos.kassza;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BankjegyServiceTest {


    @Autowired
    private BankjegyService bankjegyService;

    private List<Bankjegy> bankjegyek = List.of(
            Bankjegy.builder()
                    .ertek(10000)
                    .nev("Tízezer")
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(5000)
                    .nev("Ötzezer")
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(2000)
                    .nev("Kétezer")
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(1000)
                    .nev("Ezer")
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(500)
                    .nev("Ötszáz")
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(200)
                    .nev("Kétszáz")
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(100)
                    .nev("Száz")
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(50)
                    .nev("Ötven")
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(20)
                    .nev("Húsz")
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(10)
                    .nev("Tíz")
                    .mennyiseg(3)
                    .build(),
            Bankjegy.builder()
                    .ertek(5)
                    .nev("Öt")
                    .mennyiseg(3)
                    .build()
    );

    @BeforeEach
    void setUp() {
        bankjegyService.save(bankjegyek);
    }

    @AfterEach
    void tearDown() {
        bankjegyService.deleteAll();
    }

    @Disabled
    @Test
    void visszaadas() {
        assertEquals(
                "{" +
                        "otszazas=1," +
                        " szazas=0," +
                        " ezres=1," +
                        " tizes=1," +
                        " tizezres=1," +
                        " otezres=1," +
                        " huszas=0," +
                        " ketszazas=1," +
                        " ketezres=1," +
                        " otvenes=1," +
                        " otos=1"+
                        "}",
                bankjegyService.bankjegyek(
                        1235,
                        20000)
        );
    }
}