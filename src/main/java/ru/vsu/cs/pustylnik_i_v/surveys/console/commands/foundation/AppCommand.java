package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;

public abstract class AppCommand implements Command {

    protected final ConsoleAppContext appContext;

    public AppCommand(ConsoleAppContext appContext) {
        this.appContext = appContext;
    }

}
