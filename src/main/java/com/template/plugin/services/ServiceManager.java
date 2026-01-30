package com.template.plugin.services;

import com.template.plugin.core.PluginCore;
import java.util.ArrayList;
import java.util.List;

/**
 * Gerenciador responsável pelo ciclo de vida dos serviços.
 * Garante que todos sejam iniciados e finalizados na ordem correta.
 */
public class ServiceManager {

    private final PluginCore core;
    private final List<IService> services = new ArrayList<>();

    public ServiceManager(PluginCore core) {
        this.core = core;
    }

    /**
     * Registra e inicializa um serviço imediatamente.
     * Também o adiciona ao Core para injeção.
     */
    public <T extends IService> T register(Class<? super T> type, T service) {
        // Registra no container de DI
        core.registerService((Class<Object>) type, service);

        // Mantém na lista para lifecycle
        services.add(service);

        // Inicializa
        try {
            service.onEnable(core);
        } catch (Exception e) {
            core.getLogger().severe("Failed to enable service: " + type.getSimpleName());
            e.printStackTrace();
        }

        return service;
    }

    public <T> T getService(Class<T> type) {
        return core.getService(type);
    }

    public void reloadAll() {
        for (IService service : services) {
            try {
                service.reload();
            } catch (Exception e) {
                core.getLogger().severe("Failed to reload service: " + service.getClass().getSimpleName());
                e.printStackTrace();
            }
        }
    }

    public void disableAll() {
        // Desliga na ordem inversa de registro (pilha)
        for (int i = services.size() - 1; i >= 0; i--) {
            try {
                services.get(i).onDisable();
            } catch (Exception e) {
                core.getLogger().severe("Failed to disable service: " + services.get(i).getClass().getSimpleName());
                e.printStackTrace();
            }
        }
        services.clear();
    }
}
