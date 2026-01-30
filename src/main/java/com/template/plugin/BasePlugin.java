package com.template.plugin;

import com.template.plugin.services.MessageService;
import org.bukkit.plugin.java.JavaPlugin;

public class BasePlugin extends JavaPlugin {

    private static BasePlugin instance;
    private AppContext appContext;

    @Override
    public void onEnable() {
        instance = this;

        // Initialize Context
        this.appContext = new AppContext(this);
        this.appContext.initialize();

        getLogger().info("BasePlugin enabled successfully!");
    }

    @Override
    public void onDisable() {
        if (this.appContext != null) {
            this.appContext.shutdown();
        }
        getLogger().info("BasePlugin disabled.");
    }

    public static BasePlugin getInstance() {
        return instance;
    }

    public AppContext getAppContext() {
        return appContext;
    }

    public com.template.plugin.core.PluginCore getCore() {
        return appContext.getCore();
    }
}
