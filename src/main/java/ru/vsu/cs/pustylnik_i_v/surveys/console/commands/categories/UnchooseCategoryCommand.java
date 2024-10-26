package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.categories;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;

public class UnchooseCategoryCommand extends AppCommand {

    public UnchooseCategoryCommand(ConsoleAppContext appContext) {
        super(appContext);
    }

    @Override
    public String getName() {
        return "Unselect category";
    }

    @Override
    public void execute() {
        appContext.currentCategory = null;
        System.out.println("Category was unselected");
        appContext.getCommandExecutor().getCommand(CommandType.LIST_CATEGORIES).execute();
    }
}
