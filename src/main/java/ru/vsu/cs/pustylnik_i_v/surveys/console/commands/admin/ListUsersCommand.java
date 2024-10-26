package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.admin;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class ListUsersCommand extends CommandMenu {

    private static final int perPageAmount = 7;

    public ListUsersCommand(ConsoleAppContext appContext) {
        super(new ArrayList<>(), appContext);
        setTitle("Users:");
    }

    @Override
    public String getName() {
        return "Users";
    }

    @Override
    public void execute() {
        commands = new ArrayList<>();

        appContext.selectedUser = null;

        int currentPage = appContext.currentPageIndex;

        ResponseEntity<PagedEntity<List<User>>> response = appContext.getUserInfoService().getUsersPagedList(currentPage, perPageAmount);

        PagedEntity<List<User>> usersPage = response.getBody();
        int totalPages = usersPage.getSize();
        List<User> users = usersPage.getPage();

        users.forEach(s -> commands.add(CommandType.OPEN_USER));
        if (currentPage > 0) {
            commands.add(CommandType.PREVIOUS_PAGE);
        }
        if (currentPage < totalPages - 1) {
            commands.add(CommandType.NEXT_PAGE);
        }
        commands.add(CommandType.MAIN_MENU);

        System.out.println("-----------------------------");
        System.out.println(this.getTitle() != null ? this.getTitle() : "Commands:");
        System.out.println("-----------------------------");

        int i = 0;
        if (!users.isEmpty()) {
            while (i < users.size()) {
                System.out.printf("[%d] %s\n", i, users.get(i).getName());
                i++;
            }
            System.out.println();
            System.out.printf("Page %d out of %d\n", currentPage + 1, totalPages);
        } else {
            System.out.println("No users found");
        }
        System.out.println();

        if (currentPage > 0) {
            System.out.printf("[%d] %s\n", i++, appContext.getCommandExecutor().getCommand(CommandType.PREVIOUS_PAGE).getName());
        }
        if (currentPage < totalPages - 1) {
            System.out.printf("[%d] %s\n", i++, appContext.getCommandExecutor().getCommand(CommandType.NEXT_PAGE).getName());
        }
        System.out.printf("[%d] %s\n", i, appContext.getCommandExecutor().getCommand(CommandType.MAIN_MENU).getName());

        System.out.println("-----------------------------");

        Integer input = ConsoleUtils.inputInt("a menu item number");

        if (input == null || input > commands.size() - 1 || input < 0) {
            appContext.getCommandExecutor().getCommand(CommandType.UNKNOWN).execute();
            this.execute();
            return;
        }

        if (input < users.size()) {
            appContext.selectedUser = users.get(input);
        }
        ConsoleUtils.clear();
        appContext.getCommandExecutor().getCommand(commands.get(input)).execute();
    }
}
