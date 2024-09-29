package ru.vsu.cs.pustylnik_i_v.surveys.exceptions;

public class OptionNotFoundException extends Exception {
    public OptionNotFoundException(int optionId) {
        super("Option with id " + optionId + " not found");
    }
}
