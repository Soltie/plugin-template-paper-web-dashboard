package com.template.plugin.services.core.interfaces;

import com.template.plugin.services.IService;
import java.util.Map;

/**
 * Interface generic para o sistema de Dashboard.
 * Permite o envio de eventos e logs para uma plataforma web externa de forma
 * escalável.
 */
public interface IDashboardService extends IService {

  /**
   * Envia um evento genérico para a API web de forma assíncrona.
   *
   * @param type O tipo do evento (ex: "CHAT", "JOIN", "STATS").
   * @param data Um mapa de chave-valor representando os dados do evento.
   */
  void sendEvent(String type, Map<String, Object> data);

  /**
   * Envia uma mensagem de log para o dashboard web.
   *
   * @param message A mensagem a ser enviada.
   */
  void sendLog(String message);

  /**
   * Verifica se a integração web está habilitada.
   */
  boolean isEnabled();
}
