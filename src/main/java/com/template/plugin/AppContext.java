package com.template.plugin;

import com.template.plugin.core.PluginCore;
import com.template.plugin.events.internal.EventBus;
import com.template.plugin.listeners.ChatListener;
import com.template.plugin.listeners.PlayerLifecycleListener;
import com.template.plugin.services.ServiceManager;

// Core Domain
import com.template.plugin.services.core.interfaces.*;
import com.template.plugin.services.core.impl.*;

// Engine Domain
import com.template.plugin.services.engine.interfaces.*;
import com.template.plugin.services.engine.impl.*;

// Visual Domain
import com.template.plugin.services.visual.interfaces.*;
import com.template.plugin.services.visual.impl.*;

import org.bukkit.plugin.java.JavaPlugin;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import com.template.plugin.commands.SystemCheckCommand;

public class AppContext {

    private final JavaPlugin plugin;
    private final PluginCore core;
    private final ServiceManager serviceManager;

    public AppContext(JavaPlugin plugin) {
        this.plugin = plugin;
        this.core = new PluginCore(plugin);
        this.serviceManager = new ServiceManager(core);
    }

    public void initialize() {
        // 1. Register Infrastructure Services
        serviceManager.register(EventBus.class, new EventBus());

        // 2. Register Core Services (Order matters for dependencies)
        serviceManager.register(IConfigService.class, new ConfigServiceImpl());
        serviceManager.register(IDebugService.class, new DebugServiceImpl());
        serviceManager.register(IMessageService.class, new MessageServiceImpl());
        serviceManager.register(IPlaceholderService.class, new PlaceholderServiceImpl());
        serviceManager.register(IPermissionService.class, new PermissionServiceImpl());
        serviceManager.register(ITaskService.class, new TaskServiceImpl());
        serviceManager.register(IUserService.class, (IUserService) new UserServiceImpl());
        serviceManager.register(IDashboardService.class, new DashboardServiceImpl());

        // 3. Register Visual Services
        serviceManager.register(ICooldownService.class, new CooldownServiceImpl());
        serviceManager.register(IGameStateService.class, new GameStateServiceImpl());
        serviceManager.register(IWorldService.class, new WorldServiceImpl());
        serviceManager.register(ITeamService.class, new TeamServiceImpl());
        serviceManager.register(IScoreboardService.class, new SimpleScoreboardService());
        serviceManager.register(IChatService.class, new ChatServiceImpl());
        serviceManager.register(ITabService.class, new TabServiceImpl());
        serviceManager.register(IGuiService.class, new GuiServiceImpl());

        // 4. Register Listeners (Injecting services where needed)
        plugin.getServer().getPluginManager().registerEvents(new PlayerLifecycleListener(core), plugin);
        plugin.getServer().getPluginManager().registerEvents(new com.template.plugin.listeners.VisualListener(core),
                plugin);
        plugin.getServer().getPluginManager()
                .registerEvents(new ChatListener(serviceManager.getService(IChatService.class)), plugin);
        plugin.getServer().getPluginManager().registerEvents(new com.template.plugin.listeners.GuiListener(), plugin);
        plugin.getServer().getPluginManager()
                .registerEvents(new com.template.plugin.listeners.DashboardDataListener(core), plugin);

        // 5. Register Commands
        registerCommands();

        plugin.getLogger().info("All services initialized.");
    }

    private void registerCommands() {
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register("syscheck", "Verifies status of services", new SystemCheckCommand(core));
        });
    }

    public void shutdown() {
        serviceManager.disableAll();
    }

    public PluginCore getCore() {
        return core;
    }
}
