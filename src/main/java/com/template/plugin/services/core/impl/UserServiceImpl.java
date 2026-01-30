package com.template.plugin.services.core.impl;

import com.template.plugin.core.PluginCore;
import com.template.plugin.models.User;
import com.template.plugin.repositories.UserRepository;
import com.template.plugin.repositories.YamlUserRepository;
import com.template.plugin.services.core.interfaces.IUserService;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementação padrão do serviço de usuários.
 * Utiliza UserRepository para persistência.
 */
public class UserServiceImpl implements IUserService<User> {

    private final Map<UUID, User> cache = new ConcurrentHashMap<>();
    private PluginCore core;
    private UserRepository repository;

    @Override
    public void onEnable(PluginCore core) {
        this.core = core;
        // In a strict DI system, this should be injected.
        // For now, we instantiate the specific implementation here.
        this.repository = new YamlUserRepository(core.getPlugin());
    }

    @Override
    public void onDisable() {
        // Save all cached users before clearing?
        cache.clear();
    }

    @Override
    public CompletableFuture<User> loadUser(UUID uuid) {
        return repository.findById(uuid).thenApply(optUser -> {
            User user = optUser.orElse(new User(uuid)); // Create new if not found
            cache.put(uuid, user);
            return user;
        });
    }

    @Override
    public Optional<User> getUser(UUID uuid) {
        return Optional.ofNullable(cache.get(uuid));
    }

    @Override
    public CompletableFuture<Void> saveUser(UUID uuid) {
        User user = cache.get(uuid);
        if (user == null)
            return CompletableFuture.completedFuture(null);
        return repository.save(user);
    }

    @Override
    public void invalidate(UUID uuid) {
        cache.remove(uuid);
    }
}
