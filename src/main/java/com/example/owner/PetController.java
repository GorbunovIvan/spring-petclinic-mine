package com.example.owner;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/owners/{ownerId}")
@RequiredArgsConstructor
public class PetController {

    private final OwnerRepository ownerRepository;

    @InitBinder("pet")
    private void initPetBinder(WebDataBinder binder) {
        binder.setValidator(new PetValidator());
    }

    @GetMapping("/pets/new")
    public String initCreationForm(@PathVariable Integer ownerId, Model model) {

        var owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner with id '" + ownerId + "' is not found"));

        model.addAttribute("owner", owner);
        model.addAttribute("pet", new Pet());

        return "pets/createOrUpdatePetForm";
    }

    @PostMapping("/pets/new")
    public String processCreationForm(@PathVariable Integer ownerId,
                                      @ModelAttribute @Valid Pet pet, BindingResult bindingResult,
                                      Model model) {

        var owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner with id '" + ownerId + "' is not found"));

        if (StringUtils.hasLength(pet.getName()) && pet.isNew() && owner.getPet(pet) != null) {
            bindingResult.rejectValue("name", "duplicate", "already exists");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("owner", owner);
            model.addAttribute("pet", pet);
            return "pets/createOrUpdatePetForm";
        }

        owner.addPet(pet);
        ownerRepository.save(owner);

        return "redirect:/owners/" + ownerId;
    }

    @GetMapping("/pets/{petId}/edit")
    public String initUpdateForm(@PathVariable Integer ownerId,
                                 @PathVariable Integer petId,
                                 Model model) {

        var owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner with id '" + ownerId + "' is not found"));

        var pet = owner.getPet(petId);

        if (pet == null) {
            throw new RuntimeException("Pet with id '" + petId + "' for this owner (id='" + ownerId + "') is not found");
        }

        model.addAttribute("owner", owner);
        model.addAttribute("pet", pet);

        return "pets/createOrUpdatePetForm";
    }

    @PostMapping("/pets/{petId}/edit")
    public String processUpdateForm(@PathVariable Integer ownerId, @PathVariable Integer petId,
                                      @ModelAttribute @Valid Pet pet, BindingResult bindingResult,
                                      Model model) {

        var owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner with id '" + ownerId + "' is not found"));

        pet.setId(petId);

        if (bindingResult.hasErrors()) {
            model.addAttribute("owner", owner);
            model.addAttribute("pet", pet);
            return "pets/createOrUpdatePetForm";
        }

        owner.updatePet(petId, pet);
        ownerRepository.save(owner);

        return "redirect:/owners/" + ownerId;
    }
}
