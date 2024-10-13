package ru.vsu.cs.pustylnik_i_v.surveys.console;

import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;

public class ConsoleView {

    private final ConsoleAppContext appContext;

    public ConsoleView(ConsoleAppContext appContext) {
        this.appContext = appContext;
    }

    public void run() {
        appContext.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
    }

}
