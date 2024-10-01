package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support;

import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;

import java.util.List;

public abstract class CommandMenu extends AppCommand {
    private String title;

    protected List<CommandType> commands;

    public CommandMenu(List<CommandType> commands) {
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
        System.out.println("-----------------------------");
        System.out.println(this.title != null ? this.title : "Commands:");
        System.out.println("-----------------------------");
        for (int i = 0; i < commands.size(); i++) {
            System.out.printf("[%d] %s\n", i, factory.getCommand(commands.get(i)).getName());
        }
        System.out.println("-----------------------------");

        Integer input = ConsoleUtils.inputInt("a menu item number");

        if (input == null || input > commands.size() - 1 || input < 0) {
            ConsoleUtils.clear();
            factory.getCommand(CommandType.UNKNOWN).execute();
            this.execute();
            return;
        }

        ConsoleUtils.clear();
        factory.getCommand(commands.get(input)).execute();
    }
}
