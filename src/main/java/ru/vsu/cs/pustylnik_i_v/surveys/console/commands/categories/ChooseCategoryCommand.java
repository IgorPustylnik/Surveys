package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.categories;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;

public class ChooseCategoryCommand extends AppCommand {

    public ChooseCategoryCommand(ConsoleAppContext appContext) {
        super(appContext);
    }

    @Override
    public String getName() {
        return "Choose this category";
    }

    @Override
    public void execute() {
        appContext.currentCategory = appContext.selectedCategory;

        ConsoleUtils.clear();
        System.out.printf("Category: %s was chosen\n", appContext.currentCategory.getName());
        appContext.getCommandExecutor().getCommand(CommandType.LIST_CATEGORIES).execute();
    }
}
