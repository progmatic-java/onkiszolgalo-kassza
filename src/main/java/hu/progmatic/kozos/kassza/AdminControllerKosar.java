package hu.progmatic.kozos.kassza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AdminControllerKosar {

    @Autowired
    KosarService kosarService;

    @GetMapping("/kassza/admin")
    public String adminFelulet(Model model){
        model.addAttribute("allKosar", kosarService.findAllKosarViewDto());
        return "kassza/admin";
    }

    @PostMapping("/kassza/admin/delete/{kosarId}")
    public String deleteKosarById(Model model, @PathVariable("kosarId") Integer kosarId){
        kosarService.deleteKosarById(kosarId);
        model.addAttribute("allKosar", kosarService.findAllKosarViewDto());
        return "kassza/admin";
    }
}
