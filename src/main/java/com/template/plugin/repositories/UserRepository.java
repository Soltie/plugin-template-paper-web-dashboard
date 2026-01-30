package com.template.plugin.repositories;

import com.template.plugin.models.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface UserRepository extends Repository<User, UUID> {
    CompletableFuture<Optional<User>> findById(UUID id);

    CompletableFuture<List<User>> findAll();

    CompletableFuture<Void> save(User entity);

    CompletableFuture<Void> delete(UUID id);
    // Add specific methods if needed
}
