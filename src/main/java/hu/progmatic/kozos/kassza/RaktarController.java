package hu.progmatic.kozos.kassza;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/kassza/raktar/{id}")
    public String edit(@PathVariable Integer id, Model model) {
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
            Model model) {
        if (!bindingResult.hasErrors()) {
            try {
                termekService.create(formItem);
                refreshAllTermek(model);
                clearFormItem(model);
            }catch (Exception e){
                bindingResult.addError(new FieldError("formItem",
                        "megnevezes",
                        e.getMessage()));
            }
        }
        return items();
    }

    @PostMapping("/kassza/raktar/{id}")
    public String add(
            @PathVariable Integer id,
            @ModelAttribute("formItem") @Valid Termek formItem,
            BindingResult bindingResult,
            Model model) {
        if (!bindingResult.hasErrors()) {
                termekService.add(formItem);
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


}
