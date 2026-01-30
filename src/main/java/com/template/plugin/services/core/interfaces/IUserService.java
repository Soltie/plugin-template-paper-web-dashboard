package com.template.plugin.services.core.interfaces;

import com.template.plugin.services.IService;
import org.bukkit.entity.Player;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Serviço responsável por gerenciar dados de usuários.
 * Deve abstrair se é SQL, YAML ou Mongo.
 */
public interface IUserService<T> extends IService {

    /**
     * Carrega os dados de um jogador de forma assíncrona.
     */
    CompletableFuture<T> loadUser(UUID uuid);

    /**
     * Obtém um usuário já carregado (em cache).
     */
    Optional<T> getUser(UUID uuid);

    /**
     * Salva os dados de um usuário de forma assíncrona.
     */
    CompletableFuture<Void> saveUser(UUID uuid);

    /**
     * Remove um usuário do cache (ao sair, por exemplo).
     */
    void invalidate(UUID uuid);

}
