package hu.progmatic.kozos.kassza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class KosarListaController {

    @Autowired
    KosarService kosarService;

    @GetMapping("/kassza/admin")
    public String adminFelulet(Model model){
        model.addAttribute("kosar", kosarService.findAllKosarViewDto().stream().findFirst().orElse(KosarViewDTO.builder().build()));
        return "kassza/kosarlista";
    }

}
