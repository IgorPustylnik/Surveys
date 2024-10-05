package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppData;

public abstract class AppCommand implements Command {

    protected final ConsoleAppData appData;

    public AppCommand(ConsoleAppData appData) {
        this.appData = appData;
    }

}
