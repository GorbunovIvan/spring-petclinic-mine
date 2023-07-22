package com.example.owner;

import lombok.RequiredArgsConstructor;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class PetTypeFormatter implements Formatter<PetType> {

    private final OwnerRepository ownerRepository;

    @Override
    public PetType parse(String text, Locale locale) throws ParseException {
        return ownerRepository.findPetTypes()
                .stream()
                .filter(pType -> pType.getName().equals(text))
                .findAny()
                .orElseThrow(() -> new ParseException("type not found: " + text, 0));
    }

    @Override
    public String print(PetType object, Locale locale) {
        return object.getName();
    }
}
