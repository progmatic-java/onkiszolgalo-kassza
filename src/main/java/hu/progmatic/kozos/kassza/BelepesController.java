package hu.progmatic.kozos.kassza;

import hu.progmatic.kozos.felhasznalo.FelhasznaloService;
import hu.progmatic.kozos.felhasznalo.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BelepesController {

    @Autowired
    FelhasznaloService felhasznaloService;

    @RequestMapping("/")
    public String kassza(Model model) {
        model.addAttribute("kijelentkezesCommand", KijelentkezesCommand.builder().isJogosult(false).build());
        model.addAttribute("felhasznalonev", felhasznaloService.getNev());
        return "kassza/kezdes";
    }

    @GetMapping("/kezdes/jogosultsag")
    public String toAdmin(Model model, KijelentkezesCommand kijelentkezesCommand) {
        kijelentkezesCommand.setJogosult(!kijelentkezesCommand.isJogosult);
        model.addAttribute("kijelentkezesCommand", kijelentkezesCommand);
        return "kassza/kezdes";
    }

    @ModelAttribute("itemModifying")
    public boolean itemModifying() {
        return felhasznaloService.hasRole(UserType.Roles.ITEM_MODIFYING);
    }

    @ModelAttribute("isUserModifying")
    public boolean isUserModifying() {
        return felhasznaloService.hasRole(UserType.Roles.USER_MODIFYING);
    }

}
