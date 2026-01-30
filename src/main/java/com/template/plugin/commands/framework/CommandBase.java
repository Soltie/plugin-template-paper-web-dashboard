package com.template.plugin.commands.framework;

import com.template.plugin.core.PluginCore;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Collection;

/**
 * Classe base para comandos.
 * Implementa BasicCommand do Paper para registro dinâmico.
 */
public abstract class CommandBase implements BasicCommand {

    protected final PluginCore core;

    public CommandBase(PluginCore core) {
        this.core = core;
    }

    public abstract void execute(CommandContext context);

    public List<String> tabComplete(CommandContext context) {
        return Collections.emptyList();
    }

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        execute(new CommandContext(stack.getSender(), "command", args));
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        return tabComplete(new CommandContext(stack.getSender(), "command", args));
    }
}
