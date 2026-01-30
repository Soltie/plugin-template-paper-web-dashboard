package com.template.plugin.services.engine.impl;

import com.template.plugin.core.PluginCore;
import com.template.plugin.services.engine.interfaces.IWorldService;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class WorldServiceImpl implements IWorldService {

    private PluginCore core;

    @Override
    public void onEnable(PluginCore core) {
        this.core = core;
    }

    @Override
    public void onDisable() {
    }

    @Override
    public World loadWorld(String name) {
        if (Bukkit.getWorld(name) != null)
            return Bukkit.getWorld(name);
        return Bukkit.createWorld(new WorldCreator(name));
    }

    @Override
    public boolean unloadWorld(String name, boolean save) {
        World world = Bukkit.getWorld(name);
        if (world == null)
            return false;
        return Bukkit.unloadWorld(world, save);
    }

    @Override
    public boolean worldExists(String name) {
        // Very basic check (folder exists in server root)
        return new java.io.File(Bukkit.getWorldContainer(), name).exists();
    }

    @Override
    public void resetWorld(String name, String templatePath) {
        // Logic for resetting a world from a template
        // 1. Unload world
        unloadWorld(name, false);

        // 2. Delete existing folder
        // (Implementation omitted for safety, usually requires a recursive delete
        // utility)

        // 3. Copy template folder

        // 4. Load world
        loadWorld(name);
    }
}
