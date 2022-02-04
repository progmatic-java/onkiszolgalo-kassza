package hu.progmatic.kozos.kassza;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser
public class RaktarControllerTeszt {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TermekService termekService;

    Termek termek;

    @Test
    @DisplayName("raktar.html megjelenik")
    void raktarKezdes() throws Exception {
        mockMvc.perform(get("/kassza/raktar")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Raktár")));
    }

    @Test
    @DisplayName("A termékek listája szerepel a html-ben")
    void termekLista() throws Exception{
        mockMvc.perform(get("/kassza/raktar")).andDo(print())
                .andExpect(content().string(containsString("kóla")))
                .andExpect(content().string(containsString("kenyér")))
                .andExpect(content().string(containsString("víz")));
    }



    @Nested
public class Gombteszteles {
    @BeforeEach
    void setUp() {
        termek = termekService.create(
                Termek.builder()
                        .mennyiseg(10)
                        .vonalkod("223354535")
                        .ar(100)
                        .megnevezes("ropi")
                        .build());
    }

    @AfterEach
    void tearDown() {
        if (termek != null) {
            termekService.deleteById(termek.getId());
        }
    }

    @Test
    @DisplayName("Termék hozzáadása")
    void raktar() throws Exception {
        mockMvc.perform(get("/kassza/raktar")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Raktár")))
                .andExpect(content().string(containsString("ropi")));
    }

    @Test
    @DisplayName("Törlés tesztelése")
    void torles() throws Exception {
        mockMvc.perform(post("/kassza/raktar/delete/" + termek.getId()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Raktár")))
                .andExpect(content().string(not(containsString("ropi"))));
        termek = null;
    }
}
}
