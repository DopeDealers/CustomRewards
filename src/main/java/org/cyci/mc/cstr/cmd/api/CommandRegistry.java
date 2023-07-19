package org.cyci.mc.cstr.cmd.api;

import org.cyci.mc.cstr.Registry;
import org.cyci.mc.cstr.cmd.PlayTimeCmd;

import java.util.ArrayList;
import java.util.List;

public class CommandRegistry {
    private static List<CommandListener> commands;

    public CommandRegistry() {
        if (CommandRegistry.commands == null) {
            CommandRegistry.commands = new ArrayList<CommandListener>();
        }
        CommandRegistry.commands.add(new PlayTimeCmd());

    }

    public static CommandListener getCommand(final String name) {
        for (final CommandListener commands : getCommands()) {
            if (commands.getName().equalsIgnoreCase(name)) {
                return commands;
            }
        }
        return null;
    }

    private static List<CommandListener> getCommands() {
        return CommandRegistry.commands;
    }
}
