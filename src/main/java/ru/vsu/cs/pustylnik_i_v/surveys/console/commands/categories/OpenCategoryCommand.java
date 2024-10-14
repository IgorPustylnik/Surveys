package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.categories;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;

import java.util.ArrayList;

public class OpenCategoryCommand extends CommandMenu {

    public OpenCategoryCommand(ConsoleAppContext appContext) {
        super(new ArrayList<>(), appContext);
    }

    @Override
    public String getName() {
        return "Open category";
    }

    @Override
    public void execute() {
        commands = new ArrayList<>();

        setTitle(String.format("%s\nId:%d", appContext.selectedCategory.getName(), appContext.selectedCategory.getId()));

        commands.add(CommandType.CHOOSE_CATEGORY);
        if (appContext.roleType == RoleType.ADMIN) {
            commands.add(CommandType.DELETE_CATEGORY);
        }
        commands.add(CommandType.LIST_CATEGORIES);
        super.execute();
    }
}
