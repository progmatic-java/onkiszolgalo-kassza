package hu.progmatic.kozos.kassza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
public class KosarController {


    @Autowired
    private KosarService kosarService;

    @GetMapping("/kassza/probaolvasas")
    public String probaolvasas(Model model) {
        return "/kassza/probaolvasas";
    }

    @PostMapping("/kassza/jstest/{kosarId}")
    @ResponseBody
    public String requestTest(@RequestBody TermekMennyisegHozzaadasCommand command,
                              @PathVariable("kosarId") Integer kosarId,
                              Model model) {
        System.out.println("Teszt kiiratása vonalkód : " + command.getVonalkod());
        return "kassza/kassza";
    }

    @PostMapping("/kassza/{kosarId}/addtermekJs")
    public String addTermek(@PathVariable("kosarId") Integer kosarId,
                            @ModelAttribute("termekMennyisegHozzaadasCommand") TermekMennyisegHozzaadasCommand command,
                            Model model) {
        try {
            kosarService.addTermekMennyisegCommand(kosarId, command);
        } catch (NincsElegRaktarKeszletException | NincsIlyenTermek e) {
            model.addAttribute("isNincsElegTermek", true);
            model.addAttribute("vonalkodBeolvasasHiba", e.getMessage());
        }
        model.addAttribute("allTermek", kosarService.findAllTermekNotNullMennyiseg());
        model.addAttribute("kosar", kosarService.getKosarViewDTOById(kosarId));
        model.addAttribute("termekMennyisegHozzaadasCommand", new TermekMennyisegHozzaadasCommand());
        return "kassza/kassza";
    }
    /*@PostMapping("/kassza/js/{kosarId}")
    //@ResponseBody
    public ModelAndView probaolvasPost(@RequestBody TermekMennyisegHozzaadasCommand command,
                                       @PathVariable("kosarId") Integer kosarId,
                                       Model model){
        kosarService.addTermekMennyisegCommand(kosarId, command);
        model.addAttribute("allTermek", kosarService.findAllTermekNotNullMennyiseg());
        model.addAttribute("termekMennyisegHozzaadasCommand", new TermekMennyisegHozzaadasCommand());
        model.addAttribute("kosar", kosarService.getKosarViewDTOById(kosarId));
        ModelAndView mav = new ModelAndView();
        mav.setViewName(String.format("redirect:/kassza/%s", kosarId));
        return mav;
        //return kosarIdIndex(kosarId, model);
        //return "/kassza/kassza";
        //return String.format("redirect:kassza/%s", kosarId);
    }*/

    @GetMapping("/kassza/kassza")
    public String kosarIndex(Model model) {
        KosarViewDTO kosarViewDTO = kosarService.kosarViewCreate();
        model.addAttribute("kosar", kosarViewDTO);
        model.addAttribute("isTermekMennyisegEdit", false);
        return "kassza/kassza";
    }

    @GetMapping("/kassza/{kosarId}")
    public String kosarIdIndex(@PathVariable("kosarId") Integer kosarId,
                               Model model) {
        model.addAttribute("kosar", kosarService.getKosarViewDTOById(kosarId));
        return "kassza/kassza";
    }

    @PostMapping("/kassza/torles/{kosarId}")
    public String kosarTorles(@PathVariable("kosarId") Integer kosarId) {
        kosarService.kosarDeleteById(kosarId);
        return "kassza/kezdes";
    }

    @PostMapping("/kassza/{kosarId}/addtermek")
    public String addTermek(
            @PathVariable("kosarId") Integer kosarId,
            @ModelAttribute("termekMennyisegHozzaadasCommand") @Valid TermekMennyisegHozzaadasCommand command,
            BindingResult bindingResult,
            Model model) {
        if (!bindingResult.hasErrors()) {
            try {
                kosarService.addTermekMennyisegCommand(kosarId, command);
                model.addAttribute("allTermek", kosarService.findAllTermekNotNullMennyiseg());
                model.addAttribute("termekMennyisegHozzaadasCommand", new TermekMennyisegHozzaadasCommand());
            } catch (NincsElegRaktarKeszletException e) {
                bindingResult.addError(new FieldError("termekMennyisegHozzaadasCommand",
                        "mennyiseg",
                        e.getMessage()));
            }
        }
        model.addAttribute("kosar", kosarService.getKosarViewDTOById(kosarId));
        return "kassza/kassza";
    }

    @PostMapping("/kassza/{kosarId}/modosit")
    public String modositTermek(
            @PathVariable("kosarId") Integer kosarId,
            @ModelAttribute("termekMennyisegHozzaadasCommand") @Valid TermekMennyisegHozzaadasCommand command,
            BindingResult bindingResult,
            Model model) {
        if (!bindingResult.hasErrors()) {
            try {
                kosarService.termekMennyisegModosit(kosarId, command);
                model.addAttribute("allTermek", kosarService.findAllTermekNotNullMennyiseg());
                model.addAttribute("termekMennyisegHozzaadasCommand", new TermekMennyisegHozzaadasCommand());
            } catch (NincsElegRaktarKeszletException e) {
                model.addAttribute("isTermekMennyisegEdit", true);
                model.addAttribute("allTermek", kosarService.findAllTermek());
                bindingResult.addError(new FieldError("termekMennyisegHozzaadasCommand",
                        "mennyiseg",
                        e.getMessage()));
            }
        } else {
            model.addAttribute("isTermekMennyisegEdit", true);
            model.addAttribute("allTermek", kosarService.findAllTermek());
        }
        model.addAttribute("kosar", kosarService.getKosarViewDTOById(kosarId));
        return "kassza/kassza";
    }


    @GetMapping("/kassza/{id}/kep")
    public void kep(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        TermekMennyisegDto dto = kosarService.getKosarViewDTOById(id).getUtolsoHozzaadottTermekmennyisegDto();
        response.setContentType(dto.getContentType());
        response.getOutputStream().write(dto.getKepAdat());
    }


    @PostMapping("/kassza/{kosarId}/delete/{termekMId}")
    public String deleteTermekMennyiseg(
            @PathVariable("kosarId") Integer kosarId,
            @PathVariable("termekMId") Integer termekMId,
            Model model) {
        KosarViewDTO kosarViewDTO = kosarService.deleteTermekMennyiseg(kosarId, termekMId);
        model.addAttribute("kosar", kosarViewDTO);
        model.addAttribute("allTermek", kosarService.findAllTermekNotNullMennyiseg());
        return "kassza/kassza";
    }

    @GetMapping("/kassza/{kosarId}/modosit/{termekMId}")
    public String editTermekMennyiseg(
            @PathVariable("kosarId") Integer kosarId,
            @PathVariable("termekMId") Integer termekMId,
            Model model) {
        model.addAttribute("kosar", kosarService.getKosarViewDTOById(kosarId));
        model.addAttribute("termekMennyisegHozzaadasCommand", kosarService.kivalasztottTermekHozzaad(kosarId, termekMId));
        model.addAttribute("isTermekMennyisegEdit", true);
        model.addAttribute("allTermek", kosarService.findAllTermek());
        return "kassza/kassza";
    }


    @ModelAttribute("allTermek")
    List<Termek> allKosar() {
        return kosarService.findAllTermekNotNullMennyiseg();
    }


    @ModelAttribute("termekMennyisegHozzaadasCommand")
    TermekMennyisegHozzaadasCommand termekMennyisegHozzaadasCommand() {
        return new TermekMennyisegHozzaadasCommand();
    }

    @ModelAttribute("isNincsElegTermek")
    boolean nicsElegTermek() {
        return false;
    }

    @ModelAttribute("vonalkodBeolvasasHiba")
    String vonalkodBeolvasasHiba() {
        return null;
    }

    /*@ModelAttribute("isTermekMennyisegEdit")
    boolean isTermekMennyisegEdit(){
        return false;
    }*/
}

