package hu.progmatic.kozos.kassza.controllertest;


import hu.progmatic.kozos.kassza.KosarService;
import hu.progmatic.kozos.kassza.KosarViewDTO;
import hu.progmatic.kozos.kassza.TermekMennyisegHozzaadasCommand;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser
public class FizetesControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    KosarService kosarService;

    @Nested
    class ToSzamla {
        KosarViewDTO kosarViewDTO = kosarService.kosarViewCreate();

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
