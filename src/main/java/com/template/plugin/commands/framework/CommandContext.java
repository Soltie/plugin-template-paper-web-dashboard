package com.template.plugin.commands.framework;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandContext {

    private final CommandSender sender;
    private final String[] args;
    private final String label;

    public CommandContext(CommandSender sender, String label, String[] args) {
        this.sender = sender;
        this.label = label;
        this.args = args;
    }

    public CommandSender getSender() {
        return sender;
    }

    public Player getPlayer() {
        if (sender instanceof Player) {
            return (Player) sender;
        }
        return null;
    }

    public boolean isPlayer() {
        return sender instanceof Player;
    }

    public String[] getArgs() {
        return args;
    }

    public String getLabel() {
        return label;
    }

    public String getArg(int index) {
        if (index >= 0 && index < args.length) {
            return args[index];
        }
        return null;
    }

    public int getLength() {
        return args.length;
    }
}
