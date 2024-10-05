package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppData;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;

import java.util.List;
import java.util.stream.Collectors;

public abstract class CommandMenu extends AppCommand {
    private String title;

    protected List<CommandType> commands;

    public CommandMenu(List<CommandType> commands, ConsoleAppData appData) {
        super(appData);
        this.commands = commands;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void execute() {
        printMenu(
        commands
                .stream()
                .map(type -> appData.getCommandExecutor().getCommand(type).getName())
                .collect(Collectors.toList())
        );

        Integer input = ConsoleUtils.inputInt("a menu item number");

        if (input == null || input > commands.size() - 1 || input < 0) {
            ConsoleUtils.clear();
            appData.getCommandExecutor().getCommand(CommandType.UNKNOWN).execute();
            this.execute();
            return;
        }

        ConsoleUtils.clear();
        appData.getCommandExecutor().getCommand(commands.get(input)).execute();
    }

    protected void printMenu(List<String> elements) {
        System.out.println("-----------------------------");
        System.out.println(this.title != null ? this.title : "Commands:");
        System.out.println("-----------------------------");
        for (int i = 0; i < elements.size(); i++) {
            System.out.printf("[%d] %s\n", i, elements.get(i));
        }
        System.out.println("-----------------------------");
    }
}
