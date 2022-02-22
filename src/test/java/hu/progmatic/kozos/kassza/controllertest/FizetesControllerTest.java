package hu.progmatic.kozos.kassza.controllertest;


import hu.progmatic.kozos.kassza.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser()
public class FizetesControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    KosarService kosarService;

    @Autowired
    private TermekService termekService;


    @Nested
    @WithUserDetails("customer")
    class ToSzamla {
        KosarViewDTO kosarViewDTO = kosarService.kosarViewCreate();
        Termek termek;

        @BeforeEach
        void beforeEach() {
            Termek termek = Termek.builder().vonalkod("01").mennyiseg(30).megnevezes("Teszt1").ar(1000).build();
            this.termek = termekService.create(termek);
            TermekMennyisegHozzaadasCommand termekMennyisegHozzaadasCommand = TermekMennyisegHozzaadasCommand.builder()
                    .mennyiseg(5)
                    .vonalkod("01")
                    .build();
            kosarViewDTO = kosarService.addTermekMennyisegCommand(kosarViewDTO.getKosarId(), termekMennyisegHozzaadasCommand);

        }

        @AfterEach
        void tearDown() {
            if (kosarViewDTO != null) {
                kosarService.deleteKosarById(kosarViewDTO.getKosarId());
                kosarViewDTO = null;
            }
            termekService.deleteById(termek.getId());
        }

        @Test
        @DisplayName("szamla.html megjelenik")
        void szamlaKezdes() throws Exception {
            mockMvc.perform(get("/kassza/fizetes/" + kosarViewDTO.getKosarId())).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString("Számla")));
        }

        @Test
        @DisplayName("szamla.html megjelenik")
        void toKeszpenzesFizetes() throws Exception {
            mockMvc.perform(get("/kassza/keszpenzesfizetes/" + kosarViewDTO.getKosarId())).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString("Fizetendő összeg")));
        }

        @Test
        @DisplayName("szamla.html megjelenik")
        void toBankkartyasFizetes() throws Exception {
            mockMvc.perform(get("/kassza/bankkartyaUrlap/" + kosarViewDTO.getKosarId())).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString("Keresztnév")));
        }



    }
}
