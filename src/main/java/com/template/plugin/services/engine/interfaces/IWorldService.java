package com.template.plugin.services.engine.interfaces;

import com.template.plugin.services.IService;
import org.bukkit.World;

public interface IWorldService extends IService {
    World loadWorld(String name);

    boolean unloadWorld(String name, boolean save);

    boolean worldExists(String name);

    void resetWorld(String name, String templatePath);
}
