package com.template.plugin.events.internal;

import com.template.plugin.core.PluginCore;
import com.template.plugin.services.IService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Barramento de eventos interno.
 * Permite que serviços se comuniquem sem acoplamento direto.
 */
public class EventBus implements IService {

    private final Map<Class<?>, List<Consumer<Object>>> subscribers = new HashMap<>();

    @Override
    public void onEnable(PluginCore core) {
        // Nada a fazer no enable
    }

    @Override
    public void onDisable() {
        subscribers.clear();
    }

    /**
     * Inscreve um listener para um tipo de evento específico.
     */
    public <T> void subscribe(Class<T> eventType, Consumer<T> handler) {
        subscribers.computeIfAbsent(eventType, k -> new ArrayList<>()).add((Consumer<Object>) handler);
    }

    /**
     * Publica um evento para todos os ouvintes registrados.
     */
    public void publish(Object event) {
        List<Consumer<Object>> handlers = subscribers.get(event.getClass());
        if (handlers != null) {
            for (Consumer<Object> handler : handlers) {
                try {
                    handler.accept(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
