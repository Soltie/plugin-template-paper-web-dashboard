package com.template.plugin.core;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Ponto central de dependências do plugin.
 * Responsável por armazenar e fornecer acesso a serviços e instâncias globais.
 * Evita o uso de Singleton estático para tudo, favorecendo injeção.
 */
public class PluginCore {

    private final JavaPlugin plugin;
    private final Map<Class<?>, Object> services = new HashMap<>();

    public PluginCore(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Registra um serviço no container.
     * 
     * @param type     Classe/Interface do serviço.
     * @param instance Instância do serviço.
     */
    public <T> void registerService(Class<T> type, T instance) {
        services.put(type, instance);
        plugin.getLogger().info("[Core] Service registered: " + type.getSimpleName());
    }

    /**
     * Obtém um serviço registrado.
     * 
     * @param type Classe/Interface do serviço.
     * @return O serviço instanciado, se existir.
     * @throws IllegalArgumentException se o serviço não for encontrado (fail-fast).
     */
    public <T> T getService(Class<T> type) {
        Object service = services.get(type);
        if (service == null) {
            throw new IllegalArgumentException("Service not found: " + type.getName());
        }
        return type.cast(service);
    }

    /**
     * Obtém um serviço de forma opcional (seguro para integrações opcionais).
     */
    public <T> Optional<T> getServiceOptional(Class<T> type) {
        return Optional.ofNullable(type.cast(services.get(type)));
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public Logger getLogger() {
        return plugin.getLogger();
    }
}
