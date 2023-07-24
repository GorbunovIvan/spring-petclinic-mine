package com.example.owner;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequestMapping("/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerRepository ownerRepository;

    @GetMapping("/{id}")
    public String showOwner(@PathVariable Integer id, Model model) {

        var owner = ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("owner with id '" + id + "' is now found"));

        model.addAttribute("owner", owner);
        return "owners/ownerDetails";
    }

    @GetMapping
    public String showOwners(@RequestParam(required = false) Integer page, Model model) {
        Pageable pageable = PageRequest.of(Objects.requireNonNullElse(page, 0), 3);
        model.addAttribute("owners", ownerRepository.findAll(pageable));
        return "owners/ownersList";
    }

    @GetMapping("/find")
    public String processFindForm(@RequestParam String text, Pageable pageable, Model model) {

        if (text.isBlank()) {
            return "redirect:/owners";
        }

        Page<Owner> owners = ownerRepository.findByNameLike(text, pageable);

        model.addAttribute("ownerNameSearch", text);
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

        var owner = ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("owner with id '" + id + "' is now found"));

        model.addAttribute("owner", owner);
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
