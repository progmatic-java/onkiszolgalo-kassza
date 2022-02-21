package hu.progmatic.kozos.kassza;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BelepesController {

    @RequestMapping("/")
    public String kassza(Model model) {
        model.addAttribute("adminJogosultsagCommand", AdminJogosultsagCommand.builder().isJogosult(false).build());
        return "kassza/kezdes";
    }

    @GetMapping("/kezdes/jogosultsag")
    public String toAdmin(Model model) {
        model.addAttribute("adminJogosultsagCommand", AdminJogosultsagCommand.builder().isJogosult(true).build());
        return "kassza/kezdes";
    }

}
