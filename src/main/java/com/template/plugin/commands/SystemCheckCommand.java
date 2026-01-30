package com.template.plugin.commands;

import com.template.plugin.commands.framework.CommandBase;
import com.template.plugin.commands.framework.CommandContext;
import com.template.plugin.core.PluginCore;
import com.template.plugin.services.core.interfaces.*;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class SystemCheckCommand extends CommandBase {

    public SystemCheckCommand(PluginCore core) {
        super(core);
    }

    @Override
    public void execute(CommandContext context) {
        context.getSender()
                .sendMessage(MiniMessage.miniMessage().deserialize("<yellow>=== Template System Check ===</yellow>"));

        checkService(context, "User Service", IUserService.class);
        checkService(context, "Permission Service", IPermissionService.class);
        checkService(context, "Message Service", IMessageService.class);
        checkService(context, "Config Service", IConfigService.class);
        checkService(context, "Task Service", ITaskService.class);

        context.getSender()
                .sendMessage(MiniMessage.miniMessage().deserialize("<yellow>=============================</yellow>"));
    }

    private void checkService(CommandContext context, String name, Class<?> serviceClass) {
        try {
            Object service = core.getService(serviceClass);
            context.getSender().sendMessage(
                    MiniMessage.miniMessage().deserialize(
                            "<green>[✔] " + name + " is ACTIVE (" + service.getClass().getSimpleName() + ")</green>"));
        } catch (Exception e) {
            context.getSender()
                    .sendMessage(MiniMessage.miniMessage().deserialize("<red>[✘] " + name + " is MISSING</red>"));
        }
    }
}
