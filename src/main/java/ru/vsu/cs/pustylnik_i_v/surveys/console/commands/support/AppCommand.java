package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppData;

public abstract class AppCommand implements Command {

    protected final ConsoleAppData appData = ConsoleAppData.getInstance();

    protected static final CommandFactory factory = CommandFactory.getInstance();

}
