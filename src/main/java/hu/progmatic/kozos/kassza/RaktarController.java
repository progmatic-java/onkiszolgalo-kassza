package hu.progmatic.kozos.kassza;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class RaktarController {

    @Autowired
    private TermekService termekService;

    @GetMapping("/kassza/raktar")
    public String items(Model model) {
        return items();
    }

    private String items() {
        return "/kassza/raktar";
    }

    @GetMapping("/kassza/raktar/{id}/a")
    public String edit(@PathVariable Integer id, Model model, HttpServletResponse response) throws IOException {
        KepMegjelenitesDto dto = termekService.getKepMegjelenitesDto(id);
        response.setContentType(dto.getContentType());
        response.getOutputStream().write(dto.getKepAdat());
        Termek formItem = termekService.getById(id);
        model.addAttribute("formItem", formItem);
        return "/kassza/raktar";
    }


    @PostMapping("/kassza/raktar/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        termekService.deleteById(id);
        refreshAllTermek(model);
        return items();
    }

    @PostMapping("/kassza/raktar")
    public String create(
            @ModelAttribute("formItem") @Valid Termek formItem,
            BindingResult bindingResult,
            @ModelAttribute("kepFeltoltesCommand") KepfeltoltesCommand kepfeltoltesCommand,
            Model model) throws IOException {
        try {
            termekService.validacio(formItem);
            //refreshAllTermek(model);
            //clearFormItem(model);
            model.addAttribute("formItem", formItem());
        } catch (FoglaltTermekException e) {
            for (Map.Entry<String, String> entry : e.getBindingProperty().entrySet()) {
                bindingResult.addError(new FieldError("formItem",
                        entry.getKey(),
                        entry.getValue()));
            }
        }
        if (!bindingResult.hasErrors()) {
            termekService.create(formItem,kepfeltoltesCommand);
            refreshAllTermek(model);
            clearFormItem(model);
        }
        return items();
    }

    @PostMapping("/kassza/raktar/{id}")
    public String add(
            @PathVariable Integer id,
            @ModelAttribute("kepFeltoltesCommand") KepfeltoltesCommand kepfeltoltesCommand,
            @ModelAttribute("formItem") @Valid Termek formItem,
            BindingResult bindingResult,
            Model model) throws IOException {
        if (!bindingResult.hasErrors()) {
            termekService.create(formItem, kepfeltoltesCommand);
            refreshAllTermek(model);
            clearFormItem(model);
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

    @ModelAttribute("kepFeltoltesCommand")
    KepfeltoltesCommand kepFeltoltesCommand() {
        return new KepfeltoltesCommand();
    }


}
