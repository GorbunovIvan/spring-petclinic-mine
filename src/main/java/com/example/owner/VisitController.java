package com.example.owner;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class VisitController {

    final OwnerRepository ownerRepository;

    @GetMapping("/owners/{ownerId}/pets/{petId}/visits/new")
    public String initNewVisitForm(@PathVariable Integer ownerId, @PathVariable Integer petId,
                                   Model model) {

        var owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner with id '" + ownerId + "' is not found"));

        var pet = owner.getPet(petId);

        model.addAttribute("owner", owner);
        model.addAttribute("pet", pet);
        model.addAttribute("visit", new Visit());

        return "pets/createOrUpdateVisitForm";
    }

    @PostMapping("/owners/{ownerId}/pets/{petId}/visits/new")
    public String processNewVisitForm(@PathVariable Integer ownerId, @PathVariable Integer petId,
                                      @ModelAttribute @Valid Visit visit, BindingResult bindingResult,
                                      Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("visit", visit);
            return "pets/createOrUpdateVisitForm";
        }

        var owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner with id '" + ownerId + "' is not found"));

        owner.addVisit(petId, visit);
        ownerRepository.save(owner);

        return "redirect:/owners/" + ownerId;
    }
}
