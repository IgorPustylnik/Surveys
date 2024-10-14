package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.categories.admin;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category;

public class DeleteCategory extends AppCommand {

    public DeleteCategory(ConsoleAppContext appContext) {
        super(appContext);
    }

    @Override
    public String getName() {
        return "Delete";
    }

    @Override
    public void execute() {
        Category toDelete = appContext.selectedCategory;

        Boolean confirmation = ConsoleUtils.confirm("delete this category");

        if (confirmation == null || !confirmation) {
            ConsoleUtils.clear();
            appContext.getCommandExecutor().getCommand(CommandType.LIST_CATEGORIES).execute();
        }

        appContext.getSurveysService().deleteCategory(toDelete.getId());

        ConsoleUtils.clear();
        System.out.printf("%s was deleted", toDelete.getName());
        appContext.getCommandExecutor().getCommand(CommandType.LIST_CATEGORIES).execute();
    }
}
