package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;

public class DatabaseErrorCommand extends AppCommand {

    public DatabaseErrorCommand(ConsoleAppContext appContext) {
        super(appContext);
    }

    @Override
    public String getName() {
        return "Database error";
    }

    @Override
    public void execute() {
        ConsoleUtils.clear();
        System.err.println("Database connection error");
        appContext.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
    }
}
