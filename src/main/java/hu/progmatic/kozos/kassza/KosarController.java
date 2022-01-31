package hu.progmatic.kozos.kassza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class KosarController {


  @Autowired
  TermekMennyisegService termekMennyisegService;

  @Autowired
  TermekService termekService;

  @GetMapping("/kassza/kassza")
  public String termekek(Model model) {
    return termekek();
  }

  private String termekek() {
    return "kassza/kassza";
  }

  @PostMapping("/kassza/kassza/addtermek")
  public String addTermek(
      @ModelAttribute("termekMennyisegHozzaadasCommand") @Valid TermekMennyisegHozzaadasCommand command,
      BindingResult bindingResult,
      Model model) {
    if (!bindingResult.hasErrors()) {
      termekMennyisegService.termekHozzaadasa(
          command.getVonalkod(),
          command.getMennyiseg()
      );
      model.addAttribute("kivalasztottTermek", termekMennyisegService.findAllDto());
    }

    return "kassza/kassza";
  }

  @PostMapping("kassza/kassza/termekek")
  public String kosarbanLevoTermekek(@ModelAttribute("allKasszaTermek") TermekMennyiseg termekMennyiseg,
                                     Model model) {
    model.addAttribute(allKosar());
    model.addAttribute(kivalasztottKosar());
    return termekek();
  }

  @ModelAttribute("allKasszaTermek")
  List<Termek> allKosar() {
    return termekService.findAll();
  }

  @ModelAttribute("kivalasztottTermek")
  List<TermekMennyisegDto> kivalasztottKosar() {
    return termekMennyisegService.findAllDto();
  }

  @ModelAttribute("termekMennyisegHozzaadasCommand")
  TermekMennyisegHozzaadasCommand termekMennyisegHozzaadasCommand() {
    return new TermekMennyisegHozzaadasCommand();
  }

  @ModelAttribute("allKosarTermek")
  KosarViewDTO kosarViewDTO(Integer kosarId){
    return termekMennyisegService.kosarViewDTO(kosarId);
  };


}

