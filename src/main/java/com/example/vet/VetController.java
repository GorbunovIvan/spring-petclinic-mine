package com.example.vet;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vets")
@RequiredArgsConstructor
public class VetController {

    private final VetRepository vetRepository;

    @GetMapping
    public String showVetList(Model model) {
        model.addAttribute("vets", vetRepository.findAll());
        return "vets/vetList";
    }
}
