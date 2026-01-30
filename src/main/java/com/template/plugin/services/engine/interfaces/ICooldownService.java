package com.template.plugin.services.engine.interfaces;

import com.template.plugin.services.IService;
import java.util.UUID;

public interface ICooldownService extends IService {
    void start(UUID uuid, String key, long durationMillis);

    boolean isInCooldown(UUID uuid, String key);

    long getRemainingTime(UUID uuid, String key);

    void clear(UUID uuid);
}
