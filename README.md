# 🛡️ PluginTemplate Enterprise Engine (Web Dashboard Edition)

> Uma engine de Minecraft robusta e escalável desenvolvida para **Paper 1.21.1+**, focada em alta performance, código limpo, arquitetura orientada a serviços e monitoramento em tempo real.

[![Paper](https://img.shields.io/badge/Paper-1.21.1+-blue)](https://papermc.io)
[![Java](https://img.shields.io/badge/Java-21+-orange)](https://openjdk.org/)
---

## ✨ Principais Funcionalidades

| Feature | Descrição |
|---------|-----------|
| **Adventure API** | Mensagens ricas com `Component` e `MiniMessage` |
| **Dynamic Commands** | Registro via `LifecycleEventManager` (sem YAML) |
| **Brigadier Support** | Tab-complete nativo com `BasicCommand` |
| **Async Chat** | Processamento moderno com `AsyncChatEvent` |
| **Join/Quit Messages** | Mensagens customizáveis via `chat.yml` |
| **Web Dashboard** | Monitoramento em tempo real (React + Socket.io) |
| **Service Architecture** | IoC completo via `ServiceManager` |
| **Hot-Reload Config** | Recarregue configs sem reiniciar o servidor |

---

## 🛠️ Configuração Rápida

### Mensagens de Entrada/Saída (`chat.yml`)
```yaml
join-message:
  enabled: true
  message: "&8[&a+&8] &7{player} entrou no servidor!"

quit-message:
  enabled: true
  message: "&8[&c-&8] &7{player} saiu do servidor."
```

### Chat Customizado
```yaml
format: "&8[{rank}] &f{player}&7: &f{message}"
hover-tooltip:
  - "&b&l{player}"
  - "&7Rank: &f{rank}"
  - "&eClick to message!"
```

### Scoreboard Dinâmica (`scoreboard.yml`)
```yaml
enabled: true
title: "&b&lSERVER"
lines:
  - "&7Online: &f{online}"
  - "&7Money: &e${money}"
update-interval: 20
```

---

## 🏗️ Arquitetura

```
com.template.plugin/
├── core/           # PluginCore (IoC container)
├── services/
│   ├── core/       # Config, Users, Tasks, Permissions
│   ├── visual/     # Chat, Scoreboard, Tab, GUI
│   └── engine/     # GameState, Teams, Cooldowns
├── listeners/      # Event handlers (delegam para services)
├── commands/       # CommandBase + framework
├── models/         # User, Role, PermissionNode
└── repositories/   # Abstração de persistência
```

### Princípios de Design
- **IoC/DI**: Nenhum `new Service()` — tudo via `core.getService()`
- **Async-First**: I/O pesado sempre fora da Main Thread
- **Repository Pattern**: Troque YAML por SQL sem alterar services
- **Separation of Concerns**: Listeners só delegam

---

## 🚀 Guia do Desenvolvedor

### Criando um Comando
```java
public class MyCommand extends CommandBase {
    public MyCommand(PluginCore core) {
        super(core);
    }

    @Override
    public void execute(CommandContext ctx) {
        ctx.getSender().sendMessage(
            MiniMessage.miniMessage().deserialize("<green>Sucesso!</green>")
        );
    }
}
```

### Registrando no AppContext
```java
plugin.getLifecycleManager().registerEventHandler(
    LifecycleEvents.COMMANDS,
    event -> event.registrar().register("mycommand", "Descrição", new MyCommand(core))
);
```

### Usando Tasks Assíncronas
```java
taskService.runAsyncThenSync(
    () -> repository.loadData(uuid),      // Async
    (data) -> player.sendMessage("OK!")   // Sync (Main Thread)
);
```

---

## 🖥️ Web Dashboard

Dashboard React para monitoramento em tempo real.

### Iniciar
```bash
./web-dashboard/start-dashboard.sh
```

### Estrutura
```
web-dashboard/
├── client/     # React + Vite (porta 5173)
└── server/     # Node.js + Socket.io (porta 3001)
```

---

## 📦 Deploy & Build

### Build Completo
```bash
mvn clean package -DskipTests
```

### Deploy Automatizado
```bash
./scripts/deploy.sh
```
> O script verifica se a porta 25565 está livre antes de copiar o JAR.

---

## 📁 Arquivos de Configuração

| Arquivo | Descrição |
|---------|-----------|
| `config.yml` | Configurações globais e storage |
| `chat.yml` | Formato de chat, join/quit, tooltips |
| `scoreboard.yml` | Sidebar dinâmica |
| `tab.yml` | Header/footer da tablist |

---

## ❌ Anti-Patterns

| ❌ Proibido | ✅ Correto |
|------------|-----------|
| `Bukkit.getScheduler()` | `ITaskService` |
| `new ServiceImpl()` | `core.getService()` |
| I/O na Main Thread | `runAsync()` |
| Lógica em Listeners | Delegar para Services |

---


## 🤝 Contribuindo

1. Fork o repositório
2. Crie uma branch (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'Add nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

---

> Desenvolvido com ❤️ para a comunidade Minecraft

