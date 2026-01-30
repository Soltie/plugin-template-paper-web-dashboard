package com.template.plugin.services.core.interfaces;

import com.template.plugin.services.IService;

public interface ITaskService extends IService {

    void runAsync(Runnable runnable);

    void runSync(Runnable runnable);

    void runLater(Runnable runnable, long delayTicks);

    void runTimer(Runnable runnable, long delayTicks, long periodTicks);

    void runTimerAsync(Runnable runnable, long delayTicks, long periodTicks);

    /**
     * Runs a task asynchronously and then executes a consumer on the main thread
     * with the result.
     */
    <T> void runAsyncThenSync(java.util.function.Supplier<T> asyncTask, java.util.function.Consumer<T> syncTask);
}
