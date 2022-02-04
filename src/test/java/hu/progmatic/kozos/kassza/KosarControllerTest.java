package hu.progmatic.kozos.kassza;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
    @DisplayName("A termékek listája szerepel a html-ben")
    void termekLista() throws Exception{
        mockMvc.perform(get("/kassza/kassza")).andDo(print())
                .andExpect(content().string(containsString("kóla")))
                .andExpect(content().string(containsString("kenyér")))
                .andExpect(content().string(containsString("víz")));
    }

    @Test
    @DisplayName("Termek hozzáadás után is megjelenik és a növelt végösszeg is")
    void Osszeg() throws Exception {
        KosarViewDTO kosarViewDTO = kosarService.kosarViewCreate();
        TermekMennyisegHozzaadasCommand termekMennyisegHozzaadasCommand = TermekMennyisegHozzaadasCommand.builder()
                .mennyiseg(5)
                .vonalkod("12345")
                .build();
        kosarViewDTO = kosarService.addTermekMennyisegCommand(kosarViewDTO.getKosarId(), termekMennyisegHozzaadasCommand);

        mockMvc.perform(
                        post("/kassza/" +kosarViewDTO.getKosarId() +"/addtermek")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(String.valueOf(kosarViewDTO.getKosarId()))))
                .andExpect(content().string(containsString("víz")))
                .andExpect(content().string(containsString("500")));
    }




}
