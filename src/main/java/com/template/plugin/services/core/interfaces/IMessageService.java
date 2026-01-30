package com.template.plugin.services.core.interfaces;

import com.template.plugin.services.IService;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import java.util.List;

/**
 * Serviço de mensagens e formatação.
 * Deve suportar placeholders, cores e multilinguagem (se desejado).
 */
public interface IMessageService extends IService {

    /**
     * Envia uma mensagem formatada para um alvo.
     *
     * @param target       Jogador ou Console.
     * @param key          Chave da mensagem no arquivo de configuração.
     * @param placeholders Pares chave/valor para substituição.
     */
    void sendMessage(CommandSender target, String key, String... placeholders);

    /**
     * Envia um Component diretamente para um Audience.
     */
    void sendMessage(Audience target, Component component);

    /**
     * Envia uma mensagem global (broadcast) usando Components.
     */
    void broadcast(String key, String... placeholders);

    /**
     * Retorna uma mensagem formatada como Component.
     */
    Component getComponent(String key, String... placeholders);

    /**
     * Retorna uma mensagem formatada como String legada (se necessário).
     */
    String get(String key, String... placeholders);

    /**
     * Retorna uma lista de mensagens como Components.
     */
    List<Component> getListComponent(String key, String... placeholders);

    /**
     * Formata um texto usando MiniMessage ou Legacy.
     */
    Component parse(String text);

    /**
     * Formata cores legado em um texto bruto.
     * 
     * @deprecated Use parse() para MiniMessage ou LegacyComponentSerializer.
     */
    @Deprecated
    String colorize(String text);
}
