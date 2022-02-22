package hu.progmatic.kozos.kassza.servicetest;

import hu.progmatic.kozos.kassza.EanService;
import hu.progmatic.kozos.kassza.NincsConnectionToEanServerException;
import hu.progmatic.kozos.kassza.Termek;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EanServiceTest {

    @Autowired
    EanService eanService;



    @Test
    void searchForBarcode() {
        Termek termek = eanService.searchForBarcode("3700123300014");
        assertEquals("3700123300014", termek.getVonalkod());
        assertEquals("Agua mineral Aquarel", termek.getMegnevezes());
    }

}