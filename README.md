# 🛡️ PluginTemplate Enterprise Engine (Web Dashboard Edition)

Uma engine de Minecraft robusta e escalável desenvolvida para **Paper 1.21.1+**, focada em alta performance, código limpo, arquitetura orientada a serviços e monitoramento em tempo real.

---

## 🛠️ Modernizações Recentes (Paper 1.21.1)

Esta versão foi totalmente migrada para as APIs mais recentes do Paper:
*   **Adventure API:** Integração nativa com `Component` e `MiniMessage` para mensagens ricas e formatadas.
*   **Lifecycle API:** Registro dinâmico de comandos via `LifecycleEventManager`, eliminando a necessidade de declarações estáticas no `paper-plugin.yml`.
*   **Brigadier Support:** Comandos agora implementam `BasicCommand`, permitindo suporte nativo ao sistema Brigadier (tab-complete avançado).
*   **Async Chat Event:** Processamento de chat moderno usando as novas especificações do Paper.

---

## 🖥️ Web Dashboard (Built-in)

Esta versão inclui uma dashboard web completa para monitoramento do servidor em tempo real.

*   **Backend:** Localizado em `/web-dashboard/server` (Node.js + Socket.io).
*   **Frontend:** Localizado em `/web-dashboard/client` (React + Vite).
*   **Recursos:**
    *   Monitoramento de logs em tempo real.
    *   Gráficos de performance (TPS/RAM).
    *   Gerenciamento remoto.

### Como Iniciar a Dashboard:
Execute o script na pasta raiz:
```bash
./web-dashboard/start-dashboard.sh
```

---

## 🏠 Architecture Overview

O projeto utiliza uma **Service-Oriented Architecture (SOA)** com foco em desacoplamento.

### Pilares Fundamentais:
*   **IoC (Inversion of Control):** Gerenciado pelo `ServiceManager`. Injeção de dependências facilitada via `PluginCore`.
*   **Async-First:** Priorizamos operações assíncronas para manter o TPS estável.
*   **Repository Pattern:** Abstração completa da camada de dados (YAML/SQL).
*   **Dynamic Lifecycle:** Gerenciamento moderno de recursos seguindo os padrões do Paper.

---

## 📂 Project Structure

A organização segue uma hierarquia de domínios clara:

*   `com.template.plugin`: Classe principal e contexto.
*   `services.core`: Infraestrutura (Config, Usuários, Tasks, Dashboard).
*   `services.visual`: Interface (Scoreboard, Tablist, Chat modernizado).
*   `services.engine`: Gameplay (Estados, Times, Mundos).
*   `web-dashboard`: Sistema de monitoramento externo.

---

## 🚀 Developer Guide

### 1. Criando um Novo Comando
Estenda `CommandBase` (que implementa `BasicCommand`). O registro é feito em `AppContext` via Lifecycle API.

```java
public class MyCommand extends CommandBase {
    public MyCommand(PluginCore core) {
        super(core);
    }
    @Override
    public void execute(CommandContext context) {
        context.getSender().sendMessage(MiniMessage.miniMessage().deserialize("<green>Sucesso!</green>"));
    }
}
```

### 4. Usando o Task Service (Async Safety)
**Nunca** use `Bukkit.getScheduler()`. Use o `ITaskService`.

```java
taskService.runAsyncThenSync(
    () -> userRepository.loadData(uuid),
    (data) -> player.sendMessage(Component.text("Carregado!"))
);
```

---

## ⚙️ Deployment & Scripts

Temos ferramentas de automação para facilitar o desenvolvimento:

*   `./scripts/deploy.sh`: Builder completo que verifica se a porta 25565 está aberta antes de compilar, evitando travamentos de arquivo JAR no Linux.
*   `mvn clean package`: Build padrão via Maven (o JAR é copiado automaticamente para a pasta de plugins configurada no `pom.xml`).

---

## 🛠 Instalação & Build

```bash
mvn clean package -DskipTests
```

O artefato será gerado na pasta `/target` e implantado automaticamente conforme configurado no `maven-antrun-plugin`.
