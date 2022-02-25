package hu.progmatic.kozos.kassza;


import hu.progmatic.kozos.felhasznalo.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RolesAllowed(UserType.Roles.ITEM_MODIFYING)
public class RaktarController {
    @Autowired
    private EanService eanService;

    @Autowired
    private TermekService termekService;

    @GetMapping("/kassza/raktar")
    public String items(Model model) {
        return items();
    }

    private String items() {
        return "/kassza/raktar";
    }

    @GetMapping("/kassza/raktar/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        TermekMentesCommand termekMentesCommand = termekService.getTermekMentesCommandById(id);
        model.addAttribute("termekMentesCommand", termekMentesCommand);
        return "/kassza/raktar";
    }

    @GetMapping("/kassza/raktar/{id}/kep")
    public void kep(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        KepMegjelenitesDto dto = termekService.getKepMegjelenitesDto(id);
        response.setContentType(dto.getContentType());
        response.getOutputStream().write(dto.getKepAdat());
    }


    @PostMapping("/kassza/raktar/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        termekService.deleteById(id);
        refreshAllTermek(model);
        return items();
    }


    @PostMapping("/kassza/raktar")
    public String create(
            @ModelAttribute("termekMentesCommand") @Valid TermekMentesCommand termekMentesCommand,
            BindingResult bindingResult,
            Model model) {
        try {
            termekService.validacioWithCommand(termekMentesCommand);
            //refreshAllTermek(model);
            //clearFormItem(model);
            //model.addAttribute("termekMentesCommand", termekMentesCommand());
        } catch (FoglaltTermekException e) {
            for (Map.Entry<String, String> entry : e.getBindingProperty().entrySet()) {
                bindingResult.addError(new FieldError("termekMentesCommand",
                        entry.getKey(),
                        entry.getValue()));
            }
        }
        if (!bindingResult.hasErrors()) {
            termekService.create(termekMentesCommand);
            refreshAllTermek(model);
            clearFormItem(model);
            model.addAttribute("termekMentesCommand", termekMentesCommand());
        }
        return items();
    }

    @PostMapping("/kassza/raktar/{id}")
    public String add(
            @PathVariable Integer id,
            @ModelAttribute("termekMentesCommand") @Valid TermekMentesCommand termekMentesCommand,
            BindingResult bindingResult,
            Model model) throws IOException {
        try {
            termekService.validacioWithCommand(termekMentesCommand, id);
            //refreshAllTermek(model);
            //clearFormItem(model);
            //model.addAttribute("termekMentesCommand", termekMentesCommand());
        } catch (FoglaltTermekException e) {
            for (Map.Entry<String, String> entry : e.getBindingProperty().entrySet()) {
                bindingResult.addError(new FieldError("termekMentesCommand",
                        entry.getKey(),
                        entry.getValue()));
            }
        }
        if (!bindingResult.hasErrors()) {
            termekMentesCommand.setId(id);
            termekService.update(termekMentesCommand);
            refreshAllTermek(model);
            clearFormItem(model);
            model.addAttribute("termekMentesCommand", termekMentesCommand());
        }
        return items();
    }
    @GetMapping("/kassza/raktar/addtermekJs")
    public String addVonalkod(Model model, @ModelAttribute("formItem") Termek formItem) {

        try {
            formItem = eanService.searchForBarcode(formItem.getVonalkod());
            model.addAttribute("formItem",formItem);
        }catch (NincsConnectionToEanServerException e){
            model.addAttribute("isEanApiConnectionError", true);
            model.addAttribute("formItem", new Termek());
        }
        return items();
    }

    @ModelAttribute("allItem")
    List<Termek> allItem() {
        return termekService.findAll();
    }

    @ModelAttribute("formItem")
    public Termek formItem() {
        return new Termek();
    }


    private void clearFormItem(Model model) {
        model.addAttribute("formItem", formItem());
    }

    private void refreshAllTermek(Model model) {
        model.addAttribute("allItem", allItem());
    }

    @ModelAttribute("termekMentesCommand")
    TermekMentesCommand termekMentesCommand() {
        return new TermekMentesCommand();
    }

    @ModelAttribute("isEanApiConnectionError")
    boolean isEanApiConnectionError(){
        return false;
    }

}
