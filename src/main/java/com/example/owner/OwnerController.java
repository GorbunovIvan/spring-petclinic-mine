package com.example.owner;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerRepository ownerRepository;

    @GetMapping("/{id}")
    public String showOwner(@PathVariable Integer id, Model model) {
        model.addAttribute("owner", ownerRepository.findById(id));
        return "/owners/ownerDetails";
    }

    @GetMapping("/find")
    public String initFindForm(Model model) {
        model.addAttribute("owner", new Owner());
        return "owners/findOwners";
    }

    @PostMapping("/find")
    public String processFindForm(@RequestParam String text, Model model) {

        List<Owner> owners = ownerRepository.findByLastNameStartingWith(text);

        if (owners.isEmpty()) {
            return "owners/findOwners";
        }

        model.addAttribute("owners", owners);

        return "owners/ownersList";
    }

    @GetMapping("/new")
    public String initCreationForm(Model model) {
        model.addAttribute("owner", new Owner());
        return "owners/createOrUpdateOwnerForm";
    }

    @PostMapping("/new")
    public String processCreationForm(@ModelAttribute @Valid Owner owner, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "owners/createOrUpdateOwnerForm";
        }
        ownerRepository.save(owner);
        return "redirect:/owners";
    }

    @GetMapping("/{id}/edit")
    public String initUpdateForm(@PathVariable Integer id, Model model) {
        model.addAttribute("owner", ownerRepository.findById(id));
        return "owners/createOrUpdateOwnerForm";
    }

    @PostMapping("/{id}/edit")
    public String processUpdateForm(@PathVariable Integer id,
                                    @ModelAttribute @Valid Owner owner, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "owners/createOrUpdateOwnerForm";
        }
        owner.setId(id);
        ownerRepository.save(owner);
        return "redirect:/owners/" + id;
    }
}
