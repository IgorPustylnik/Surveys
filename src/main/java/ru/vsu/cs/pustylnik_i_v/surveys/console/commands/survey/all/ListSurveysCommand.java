package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.all;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class ListSurveysCommand extends CommandMenu {

    public ListSurveysCommand(ConsoleAppContext appContext) {
        super(new ArrayList<>(), appContext);
    }

    @Override
    public String getName() {
        return "Surveys list";
    }

    @Override
    public void execute() {
        commands = new ArrayList<>();

        if (appContext.currentCategory != null) {
            setTitle(String.format("Surveys\nChosen category: %s", appContext.currentCategory.getName()));
        } else {
            setTitle("Surveys\nNo category chosen");
        }

        // Reset survey when going back from a survey page
        appContext.currentSurvey = null;

        int currentPage = appContext.currentPageIndex;
        Integer currentCategoryId = appContext.currentCategory != null ? appContext.currentCategory.getId() : null;

        ResponseEntity<PagedEntity<List<Survey>>> response = appContext.getSurveysService().getSurveysPagedList(currentCategoryId, currentPage);

        PagedEntity<List<Survey>> surveysPage = response.getBody();
        int totalPages = surveysPage.getSize();
        List<Survey> surveys = surveysPage.getPage();

        surveys.forEach(s -> commands.add(CommandType.OPEN_SURVEY));
        if (currentPage > 0) {
            commands.add(CommandType.PREVIOUS_PAGE);
        }
        if (currentPage < totalPages - 1) {
            commands.add(CommandType.NEXT_PAGE);
        }
        commands.add(CommandType.LIST_CATEGORIES);
        commands.add(CommandType.MAIN_MENU);

        System.out.println("-----------------------------");
        System.out.println(this.getTitle() != null ? this.getTitle() : "Commands:");
        System.out.println("-----------------------------");

        int i = 0;
        if (!surveys.isEmpty()) {
            while (i < surveys.size()) {
                System.out.printf("[%d] %s\n", i, surveys.get(i).getName());
                i++;
            }
            System.out.println();
            System.out.printf("Page %d out of %d\n", currentPage + 1, totalPages);
        } else {
            System.out.println("No surveys found");
        }
        System.out.println();

        if (currentPage > 0) {
            System.out.printf("[%d] %s\n", i++, appContext.getCommandExecutor().getCommand(CommandType.PREVIOUS_PAGE).getName());
        }
        if (currentPage < totalPages - 1) {
            System.out.printf("[%d] %s\n", i++, appContext.getCommandExecutor().getCommand(CommandType.NEXT_PAGE).getName());
        }
        System.out.printf("[%d] %s\n", i++, appContext.getCommandExecutor().getCommand(CommandType.LIST_CATEGORIES).getName());
        System.out.printf("[%d] %s\n", i, appContext.getCommandExecutor().getCommand(CommandType.MAIN_MENU).getName());

        System.out.println("-----------------------------");

        Integer input = ConsoleUtils.inputInt("a menu item number");

        if (input == null || input > commands.size() - 1 || input < 0) {
            appContext.getCommandExecutor().getCommand(CommandType.UNKNOWN).execute();
            this.execute();
            return;
        }

        if (input < surveys.size()) {
            appContext.currentSurvey = surveys.get(input);
        }
        ConsoleUtils.clear();
        appContext.getCommandExecutor().getCommand(commands.get(input)).execute();
    }
}
