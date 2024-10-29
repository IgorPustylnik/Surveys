package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.categories.admin;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;

public class DeleteCategoryCommand extends AppCommand {

    public DeleteCategoryCommand(ConsoleAppContext appContext) {
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
            return;
        }

        try {
            appContext.getSurveysService().deleteCategory(toDelete.getId());
        } catch (DatabaseAccessException e) {
            appContext.getCommandExecutor().getCommand(CommandType.DATABASE_ERROR).execute();
            return;
        }

        ConsoleUtils.clear();
        System.out.printf("%s was deleted\n", toDelete.getName());
        appContext.getCommandExecutor().getCommand(CommandType.LIST_CATEGORIES).execute();
    }
}
