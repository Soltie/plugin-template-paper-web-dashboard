package com.template.plugin.models;

import java.util.HashSet;
import java.util.Set;

/**
 * Modelo base de Role (Cargo).
 * Pode ser estendido ou usado como DTO.
 */
public class Role {

    private final String name;
    private final int priority;
    private final String prefix;
    private final Set<String> permissions;

    public Role(String name, int priority, String prefix) {
        this.name = name;
        this.priority = priority;
        this.prefix = prefix;
        this.permissions = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public String getPrefix() {
        return prefix;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void addPermission(String permission) {
        this.permissions.add(permission);
    }

    public boolean hasPermission(String permission) {
        return this.permissions.contains(permission) || this.permissions.contains("*");
    }
}
