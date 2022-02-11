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

    private List<Bankjegy> bankjegyBuilder(Integer tizE,
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

    @AfterEach
    void tearDown() {
        bankjegyService.deleteAll();
    }

    @Disabled
    @Test
    void visszaadas() {
        /*assertEquals(
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
        );*/
    }
}