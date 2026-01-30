package com.template.plugin.services.engine.impl;

import com.template.plugin.core.PluginCore;
import com.template.plugin.services.engine.interfaces.ICooldownService;
import com.template.plugin.services.core.interfaces.ITaskService;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownServiceImpl implements ICooldownService {

    // Map<PlayerUUID, Map<CooldownKey, ExpirationTime>>
    private final Map<UUID, Map<String, Long>> cooldowns = new ConcurrentHashMap<>();
    private ITaskService taskService;

    @Override
    public void onEnable(PluginCore core) {
        this.taskService = core.getService(ITaskService.class);
        // Optional: Scheduler task to cleanup heavy old data if needed,
        // but removing onQuit is usually enough for memory management.
    }

    @Override
    public void onDisable() {
        cooldowns.clear();
    }

    @Override
    public void start(UUID uuid, String key, long durationMillis) {
        cooldowns.computeIfAbsent(uuid, k -> new ConcurrentHashMap<>())
                .put(key, System.currentTimeMillis() + durationMillis);
    }

    @Override
    public boolean isInCooldown(UUID uuid, String key) {
        return getRemainingTime(uuid, key) > 0;
    }

    @Override
    public long getRemainingTime(UUID uuid, String key) {
        if (!cooldowns.containsKey(uuid))
            return 0;

        Map<String, Long> playerCooldowns = cooldowns.get(uuid);
        if (!playerCooldowns.containsKey(key))
            return 0;

        long expiration = playerCooldowns.get(key);
        long remaining = expiration - System.currentTimeMillis();

        if (remaining <= 0) {
            // Lazy cleanup
            playerCooldowns.remove(key);
            return 0;
        }

        return remaining;
    }

    @Override
    public void clear(UUID uuid) {
        cooldowns.remove(uuid);
    }
}
