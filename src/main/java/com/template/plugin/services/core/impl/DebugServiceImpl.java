package com.template.plugin.services.core.impl;

import com.template.plugin.core.PluginCore;
import com.template.plugin.services.core.interfaces.IConfigService;
import com.template.plugin.services.core.interfaces.IDebugService;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DebugServiceImpl implements IDebugService {

    private PluginCore core;
    private IConfigService configService;
    private Logger logger;

    @Override
    public void onEnable(PluginCore core) {
        this.core = core;
        this.configService = core.getService(IConfigService.class);
        this.logger = core.getPlugin().getLogger();
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void debug(String category, String message) {
        if (configService.getConfig().getGeneral().isDebugMode()) {
            logger.info("[DEBUG][" + category + "] " + message);
        }
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warn(String message) {
        logger.warning(message);
    }

    @Override
    public void error(String message) {
        logger.severe(message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        logger.log(Level.SEVERE, message, throwable);
    }
}
