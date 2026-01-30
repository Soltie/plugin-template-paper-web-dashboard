package com.template.plugin.services;

import com.template.plugin.core.PluginCore;

/**
 * Interface base para todos os serviços do sistema.
 * Define o ciclo de vida padrão.
 */
public interface IService {

    /**
     * Chamado quando o serviço é inicializado no onEnable.
     * 
     * @param core Instância do container principal.
     */
    void onEnable(PluginCore core);

    /**
     * Chamado quando o plugin é desligado no onDisable.
     */
    void onDisable();

    /**
     * Chamado para recarregar configurações ou estado do serviço.
     */
    default void reload() {
    }

}
