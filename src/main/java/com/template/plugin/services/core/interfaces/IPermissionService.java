package com.template.plugin.services.core.interfaces;

import com.template.plugin.services.IService;
import org.bukkit.entity.Player;

/**
 * Serviço de permissões.
 * Pode ser apenas um wrapper para o Bukkit/Vault ou um sistema próprio.
 */
public interface IPermissionService extends IService {

    boolean hasPermission(Player player, String permission);

    boolean hasPermission(Player player, String permission, boolean defaultOp);

    /**
     * Adiciona uma permissão temporária ou permanente (se suportado).
     */
    void addPermission(Player player, String permission);

    /**
     * Remove uma permissão (se suportado).
     */
    void removePermission(Player player, String permission);

    /**
     * Retorna o grupo principal do jogador (se houver sistema de grupos).
     */
    String getGroup(Player player);

}
