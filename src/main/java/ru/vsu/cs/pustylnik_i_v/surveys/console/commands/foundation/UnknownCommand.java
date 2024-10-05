package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation;

public class UnknownCommand implements Command {
    @Override
    public String getName() {
        return "Unknown command";
    }

    @Override
    public void execute() {
        System.err.println("Unknown command");
    }
}