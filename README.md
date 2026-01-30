# 🛡️ PluginTemplate Enterprise Engine

Uma engine de Minecraft robusta e escalável desenvolvida para Spigot/Paper, focada em alta performance, código limpo e arquitetura orientada a serviços.

---

## 🏠 Architecture Overview

O projeto utiliza uma **Service-Oriented Architecture (SOA)** com foco em desacoplamento e facilidade de manutenção.

### Pilares Fundamentais:
*   **IoC (Inversion of Control):** Gerenciado pelo `ServiceManager`. Nenhuma classe deve ser instanciada manualmente com `new` se ela possuir lógica de negócio; ela deve ser registrada e recuperada via `PluginCore`.
*   **Async-First:** Priorizamos operações assíncronas para manter o TPS do servidor estável. Todo processamento pesado ou I/O deve ocorrer fora da Main Thread.
*   **Repository Pattern:** A persistência de dados (YAML, SQL, etc.) é abstraída via Repositories, permitindo trocar o sistema de salvamento sem alterar a lógica dos serviços.
*   **Separation of Concerns:** Nenhuma lógica de negócio reside no `BasePlugin` ou em `Listeners`. Listeners apenas capturam eventos e delegam para os serviços apropriados.

---

## 📂 Project Structure

A organização segue uma hierarquia de domínios clara:

*   `com.template.plugin`: Contém a classe principal (`BasePlugin`) e o contexto da aplicação.
*   `services.core`: Serviços de infraestrutura global (Gerenciamento de Usuários, Configurações, Tasks Agendadas, Permissões).
*   `services.visual`: Toda a camada de interface com o usuário (Scoreboard, Tablist, Formatação de Chat, GUIs).
*   `services.engine`: Lógicas específicas de gameplay e mecânicas (Estados de Jogo, Times, Cooldowns, Mundos).
*   `models` & `repositories`: Definição de objetos de dados e contratos de persistência.

---

## 🚀 Developer Guide

### 1. Criando um Novo Comando
Estenda `CommandBase` para herdar o sistema automático de permissões e processamento de argumentos.

```java
public class MyCommand extends CommandBase {
    public MyCommand(PluginCore core) {
        super(core, "mycommand", "permission.admin");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("Comando executado com sucesso!");
    }
}
```

### 2. Adicionando uma Nova Configuração
O sistema é **Data-Driven**. Siga estes passos:
1.  Adicione o campo no `.yml` (ex: `chat.yml`).
2.  Atualize o POJO correspondente (ex: `ChatSettings`).
3.  Acesse via serviço:
```java
String format = configService.getChatSettings().getFormat();
```

### 3. Criando uma Interface (GUI)
Utilize o sistema de `Menu` para criar GUIs responsivas:

```java
public class ProfileMenu extends Menu {
    public ProfileMenu(PluginCore core) {
        super(core, "&8Seu Perfil", 3); // 3 rows
    }

    @Override
    public void setupItems() {
        setItem(13, new ItemBuilder(Material.SKULL_ITEM).setName("&aEstatísticas").build(), (event) -> {
            player.sendMessage("Abrindo estatísticas...");
        });
    }
}
```

### 4. Usando o Task Service (Async Safety)
**Nunca** use `Bukkit.getScheduler()` diretamente. Use o `ITaskService` para garantir segurança entre threads.

```java
taskService.runAsyncThenSync(
    () -> userRepository.loadData(uuid), // Executa em paralelo
    (data) -> player.sendMessage("Dados carregados: " + data) // Retorna para a Main Thread
);
```

---

## ⚙️ Configuration & Features

A maioria das funcionalidades suporta **Hot-Reload**. Ao alterar um arquivo, use o comando de reload para atualizar os POJOs em memória:

*   `config.yml`: Configurações globais e banco de dados.
*   `chat.yml`: Formatação de chat e tooltips JSON.
*   `scoreboard.yml`: Sidebar dinâmica e intervalos de atualização.
*   `tab.yml`: Cabeçalho e rodapé da lista de jogadores.

---

## ❌ Anti-Patterns (Regras de Ouro)

1.  **Proibido:** `Bukkit.getScheduler()`. **Use:** `ITaskService`.
2.  **Proibido:** `new ServiceImpl()`. **Use:** Registro no `ServiceManager`.
3.  **Proibido:** Bloquear a Main Thread com I/O ou consultas SQL.
4.  **Proibido:** Colocar lógica de comandos dentro da classe principal do plugin.

---

## 🛠 Instalação & Build

Para compilar o projeto e gerar o JAR com timestamp único:

```bash
mvn clean package -DskipTests
```

O artefato será gerado na pasta `/target`.
