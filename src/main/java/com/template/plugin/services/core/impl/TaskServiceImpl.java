package com.template.plugin.services.core.impl;

import com.template.plugin.core.PluginCore;
import com.template.plugin.services.core.interfaces.ITaskService;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public class TaskServiceImpl implements ITaskService {

    private PluginCore core;

    @Override
    public void onEnable(PluginCore core) {
        this.core = core;
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(core.getPlugin());
    }

    @Override
    public void runAsync(Runnable runnable) {
        getScheduler().runTaskAsynchronously(core.getPlugin(), runnable);
    }

    @Override
    public void runSync(Runnable runnable) {
        getScheduler().runTask(core.getPlugin(), runnable);
    }

    @Override
    public void runLater(Runnable runnable, long delayTicks) {
        getScheduler().runTaskLater(core.getPlugin(), runnable, delayTicks);
    }

    @Override
    public void runTimer(Runnable runnable, long delayTicks, long periodTicks) {
        Bukkit.getScheduler().runTaskTimer(core.getPlugin(), runnable, delayTicks, periodTicks);
    }

    @Override
    public void runTimerAsync(Runnable runnable, long delayTicks, long periodTicks) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(core.getPlugin(), runnable, delayTicks, periodTicks);
    }

    @Override
    public <T> void runAsyncThenSync(java.util.function.Supplier<T> asyncTask,
            java.util.function.Consumer<T> syncTask) {
        getScheduler().runTaskAsynchronously(core.getPlugin(), () -> {
            T result = asyncTask.get();
            getScheduler().runTask(core.getPlugin(), () -> syncTask.accept(result));
        });
    }

    private BukkitScheduler getScheduler() {
        return Bukkit.getScheduler();
    }
}
