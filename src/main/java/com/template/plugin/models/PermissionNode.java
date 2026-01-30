package com.template.plugin.models;

/**
 * Modelo simples de Permissão.
 * Útil para listar todas as permissões do sistema em um registry.
 */
public class PermissionNode {

    private final String node;
    private final String description;
    private final boolean defaultOp;

    public PermissionNode(String node, String description, boolean defaultOp) {
        this.node = node;
        this.description = description;
        this.defaultOp = defaultOp;
    }

    public String getNode() {
        return node;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDefaultOp() {
        return defaultOp;
    }
}
