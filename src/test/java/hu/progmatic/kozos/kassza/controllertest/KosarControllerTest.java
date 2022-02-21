package hu.progmatic.kozos.kassza.controllertest;


import hu.progmatic.kozos.kassza.KosarService;
import hu.progmatic.kozos.kassza.KosarViewDTO;
import hu.progmatic.kozos.kassza.TermekMennyisegDto;
import hu.progmatic.kozos.kassza.TermekMennyisegHozzaadasCommand;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser
public class KosarControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    KosarService kosarService;

    @Test
    @DisplayName("kassza.html megjelenik")
    void kosarKezdes() throws Exception {
        mockMvc.perform(get("/kassza/kassza")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Üdvözöljük!")));
    }




    @Test
    @DisplayName("probaolvasas.html megjelenik")
    void toProbaOlvasas() throws Exception {
        mockMvc.perform(get("/kassza/probaolvasas")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @DisplayName("A termékek listája szerepel a html-ben")
    void termekLista() throws Exception{
        mockMvc.perform(get("/kassza/kassza")).andDo(print())
                .andExpect(content().string(containsString("kóla")))
                .andExpect(content().string(containsString("kenyér")))
                .andExpect(content().string(containsString("víz")));
    }


    @Nested
    class Gombtesztelesek{

        private KosarViewDTO kosarViewDTO = kosarService.kosarViewCreate();
        @BeforeEach
        void beforeEach() {
            TermekMennyisegHozzaadasCommand termekMennyisegHozzaadasCommand = TermekMennyisegHozzaadasCommand.builder()
                    .mennyiseg(5)
                    .vonalkod("5051007149822")
                    .build();
            kosarViewDTO = kosarService.addTermekMennyisegCommand(kosarViewDTO.getKosarId(), termekMennyisegHozzaadasCommand);

        }

        @AfterEach
        void tearDown() {
            if (kosarViewDTO != null) {
                kosarService.deleteKosarById(kosarViewDTO.getKosarId());
                kosarViewDTO = null;
            }
        }



        @Test
        @DisplayName("Termek hozzáadás után is megjelenik és a növelt végösszeg is")
        void osszeg() throws Exception {
            mockMvc.perform(
                            post("/kassza/" + kosarViewDTO.getKosarId() + "/addtermek")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString(String.valueOf(kosarViewDTO.getKosarId()))))
                    .andExpect(content().string(containsString("víz")))
                    .andExpect(content().string(containsString("500")))
                    .andExpect(content().string(not(containsString("Aktuálisan hozzáadott termék neve"))));
        }

        @Test
        @DisplayName("Termek hozzáadás után is megjelenik és a növelt végösszeg is")
        void osszegJs() throws Exception {
            mockMvc.perform(
                            post("/kassza/" + kosarViewDTO.getKosarId() + "/addtermekJs")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString(String.valueOf(kosarViewDTO.getKosarId()))))
                    .andExpect(content().string(containsString("víz")))
                    .andExpect(content().string(containsString("500")))
                    .andExpect(content().string(not(containsString("Aktuálisan hozzáadott termék neve"))));
        }

        @Test
        @DisplayName("Termek módosítás gomb után is megjelenik")
        void modosit() throws Exception {
            mockMvc.perform(
                            post("/kassza/" + kosarViewDTO.getKosarId() + "/modosit")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString(String.valueOf(kosarViewDTO.getKosarId()))))
                    .andExpect(content().string(containsString("víz")))
                    .andExpect(content().string(containsString("500")))
                    .andExpect(content().string(not(containsString("Aktuálisan hozzáadott termék neve"))));
        }

        @Test
        @DisplayName("törlés gomb után eltűnik a termék és a végösszeg is a kosárból")
        void OsszegTorles() throws Exception {
            mockMvc.perform(post("/kassza/"+ kosarViewDTO.getKosarId() + "/delete/"
                            + kosarViewDTO.getTermekMennyisegDtoList().stream()
                            .filter(termekMennyisegDto -> termekMennyisegDto.getNev().equals("víz"))
                    .mapToInt(TermekMennyisegDto::getTermekMennyisegId).findAny().orElseThrow()))
                    .andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString("Végösszeg")))
                    .andExpect(content().string(containsString("0")))
                    .andExpect(content().string(not(containsString("500"))));
            kosarViewDTO = null;
        }


    }


@Nested
class Vissza{

    private KosarViewDTO kosarViewDTO = kosarService.kosarViewCreate();

    @Test
    @DisplayName("kezdes.html megjelenik")
    void toKezdolap() throws Exception {
        mockMvc.perform(post("/kassza/torles/" + kosarViewDTO.getKosarId())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Vásárlás kezdéséhez nyomja meg a gombot")));
    }

    @Test
    @DisplayName("kassza.html megjelenik")
    void kosarId() throws Exception {
        mockMvc.perform(get("/kassza/" + kosarViewDTO.getKosarId())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Üdvözöljük!")));
    }


}





}
