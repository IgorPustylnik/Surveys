package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.all;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppData;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class ListSurveysCommand extends CommandMenu {

    public ListSurveysCommand(ConsoleAppData appData) {
        super(new ArrayList<>(), appData);
        setTitle("Surveys");
    }

    @Override
    public String getName() {
        return "Surveys list";
    }

    @Override
    public void execute() {
        commands = new ArrayList<>();

        int currentPage = appData.currentPageIndex;
        Integer currentCategoryId = appData.category != null ? appData.category.getId() : null;

        ResponseEntity<PagedEntity<List<Survey>>> response = appData.getSurveysService().getSurveysPagedList(currentCategoryId, currentPage);

        PagedEntity<List<Survey>> surveysPage = response.getBody();
        int totalPages = surveysPage.getSize();
        List<Survey> surveys = surveysPage.getPage();

        surveys.forEach(s -> commands.add(CommandType.OPEN_SURVEY));
        if (currentPage > 1) {
            commands.add(CommandType.PREVIOUS_PAGE);
        }
        if (currentPage < totalPages) {
            commands.add(CommandType.NEXT_PAGE);
        }
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
            System.out.printf("Page %d out of %d\n", currentPage, totalPages);
        } else {
            System.out.println("No surveys found");
        }
        System.out.println();

        if (currentPage > 1) {
            System.out.printf("[%d] %s\n", i++, appData.getCommandExecutor().getCommand(CommandType.PREVIOUS_PAGE).getName());
        }
        if (currentPage < totalPages) {
            System.out.printf("[%d] %s\n", i++, appData.getCommandExecutor().getCommand(CommandType.NEXT_PAGE).getName());
        }
        System.out.printf("[%d] %s\n", i, appData.getCommandExecutor().getCommand(CommandType.MAIN_MENU).getName());

        System.out.println("-----------------------------");

        Integer input = ConsoleUtils.inputInt("a menu item number");

        if (input == null || input > commands.size() - 1 || input < 0) {
            appData.getCommandExecutor().getCommand(CommandType.UNKNOWN).execute();
            this.execute();
            return;
        }

        if (input < surveys.size()) {
            appData.currentSurvey = surveys.get(input);
        }
        ConsoleUtils.clear();
        appData.getCommandExecutor().getCommand(commands.get(input)).execute();
    }
}
