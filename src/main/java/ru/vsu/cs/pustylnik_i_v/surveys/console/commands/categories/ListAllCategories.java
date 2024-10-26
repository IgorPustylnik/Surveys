package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.categories;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class ListAllCategories extends CommandMenu {

    private static final int perPageAmount = 6;

    public ListAllCategories(ConsoleAppContext appContext) {
        super(new ArrayList<>(), appContext);
    }

    @Override
    public String getName() {
        return "Categories";
    }

    @Override
    public void execute() {
        appContext.selectedCategory = null;

        if (appContext.currentCategory != null) {
            setTitle(String.format("Categories:\nCurrently chosen: %s", appContext.currentCategory.getName()));
        } else {
            setTitle("Categories:\nCurrently chosen: none");
        }
        commands = new ArrayList<>();

        int currentPage = appContext.currentPageIndex;

        ResponseEntity<PagedEntity<List<Category>>> response = appContext.getSurveysService().getCategoriesPagedList(currentPage, perPageAmount);

        PagedEntity<List<Category>> categoriesPage = response.getBody();
        int totalPages = categoriesPage.getSize();
        List<Category> categories = categoriesPage.getPage();

        categories.forEach(s -> commands.add(CommandType.OPEN_CATEGORY));
        if (currentPage > 0) {
            commands.add(CommandType.PREVIOUS_SURVEYS_PAGE);
        }
        if (currentPage < totalPages - 1) {
            commands.add(CommandType.NEXT_SURVEYS_PAGE);
        }
        if (appContext.currentCategory != null) {
            commands.add(CommandType.UNCHOOSE_CATEGORY);
        }
        commands.add(CommandType.LIST_SURVEYS);

        System.out.println("-----------------------------");
        System.out.println(this.getTitle() != null ? this.getTitle() : "Commands:");
        System.out.println("-----------------------------");

        int i = 0;
        if (!categories.isEmpty()) {
            while (i < categories.size()) {
                System.out.printf("[%d] %s\n", i, categories.get(i).getName());
                i++;
            }
            System.out.println();
            System.out.printf("Page %d out of %d\n", currentPage + 1, totalPages);
        } else {
            System.out.println("No categories found");
        }
        System.out.println();

        if (currentPage > 0) {
            System.out.printf("[%d] %s\n", i++, appContext.getCommandExecutor().getCommand(CommandType.PREVIOUS_SURVEYS_PAGE).getName());
        }
        if (currentPage < totalPages - 1) {
            System.out.printf("[%d] %s\n", i++, appContext.getCommandExecutor().getCommand(CommandType.NEXT_SURVEYS_PAGE).getName());
        }

        if (appContext.currentCategory != null) {
            System.out.printf("[%d] Unselect category\n", i++);
        }
        System.out.printf("[%d] Back\n", i);

        System.out.println("-----------------------------");

        Integer input = ConsoleUtils.inputInt("a menu item number");

        if (input == null || input > commands.size() - 1 || input < 0) {
            appContext.getCommandExecutor().getCommand(CommandType.UNKNOWN).execute();
            this.execute();
            return;
        }

        if (input < categories.size()) {
            appContext.selectedCategory = categories.get(input);
        }
        ConsoleUtils.clear();
        appContext.getCommandExecutor().getCommand(commands.get(input)).execute();
    }
}
