package hu.progmatic.kozos.felhasznalo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
public class FelhasznaloController {
    private final FelhasznaloService felhasznaloService;

    public FelhasznaloController(FelhasznaloService felhasznaloService) {
        this.felhasznaloService = felhasznaloService;
    }

    @GetMapping("/felhasznalo")
    public String lista() {
        return "felhasznalo";
    }

    @PostMapping("/felhasznalo")
    public String add(@ModelAttribute UjFelhasznaloCommand command, Model model) {
        model.addAttribute("ujFelhasznaloError", null);
        try {
            felhasznaloService.add(command);
        } catch (FelhasznaloLetrehozasException e) {
            model.addAttribute("ujFelhasznaloError", e.getMessage());
            return "felhasznalo";
        }
        frissit(model);
        return "felhasznalo";
    }

    private void frissit(Model model) {
        model.addAttribute("allFelhasznalo", populateTypes());
    }

    @PostMapping("/felhasznalo/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        felhasznaloService.delete(id);
        frissit(model);
        return "redirect:/felhasznalo";
    }

    @GetMapping("/felhasznalo/modosit/{id}")
    public String modositBetolt(@PathVariable Long id, Model model) {
        ModositFelhasznalo modositFelhasznalo = felhasznaloService.getModositFelhasznaloById(id);
        model.addAttribute("modositFelhasznalo", modositFelhasznalo);
        return "felhasznalo";
    }

    @PostMapping("/felhasznalo/modosit/{id}")
    public String modosit(@PathVariable Long id,
                          @ModelAttribute("modositFelhasznalo") @Valid ModositFelhasznalo modositFelhasznalo,
                          Model model) {
        model.addAttribute("ujFelhasznaloError", null);
        felhasznaloService.modosit(modositFelhasznalo, id);
        model.addAttribute("allFelhasznalo", populateTypes());
        return "felhasznalo";
    }

    @ModelAttribute("modositFelhasznalo")
    public ModositFelhasznalo modositFelhasznalo() {
        return new ModositFelhasznalo();
    }

    @ModelAttribute("allFelhasznalo")
    public List<Felhasznalo> populateTypes() {
        if (felhasznaloService.hasRole(UserType.Roles.USER_MODIFYING)) {
            return felhasznaloService.findAll();
        }
        return List.of();
    }


    @ModelAttribute("ujFelhasznaloCommand")
    public UjFelhasznaloCommand ujFelhasznaloCommand() {
        return new UjFelhasznaloCommand();
    }

    @ModelAttribute("ujFelhasznaloError")
    public String ujFelhasznaloError() {
        return null;
    }

    @ModelAttribute("hasUserWriteRole")
    public boolean userWriteRole() {
        return felhasznaloService.hasRole(UserType.Roles.USER_MODIFYING);
    }

    @ModelAttribute("hasUserReadRole")
    public boolean userReadRole() {
        return felhasznaloService.hasRole(UserType.Roles.USER_MODIFYING);
    }

    @ModelAttribute("userId")
    public Long userId() {
        return felhasznaloService.getFelhasznaloId();
    }

    @ModelAttribute("allRole")
    public List<String> allRole() {
        return Arrays.stream(UserType.values())
                .map(UserType::name).toList();
    }
}
