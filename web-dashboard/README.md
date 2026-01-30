# Dashboard Web Integration

Esta pasta contém a base para o dashboard web integrado ao seu plugin.

## Estrutura
- `/server`: Backend em Node.js (Express + Socket.io) que recebe eventos do plugin e transmite para a web.
- `/client`: Frontend em React/Vite com uma estética premium (Glassmorphism).

## Como rodar

### 1. Requisitos
- [Node.js](https://nodejs.org/) instalado.

### Linux (Recomendado)
O script `start-dashboard.sh` automatiza todo o processo, verificando se o dashboard já está rodando, desligando-o se necessário e iniciando tudo do zero.
```bash
./start-dashboard.sh
```

### Manual
#### 1. Iniciando o Backend
```bash
cd client
npm install
npm run dev
```
O dashboard estará acessível em `http://localhost:5173`.

## Funcionalidades
- **Overview**: Gráficos de jogadores online e status do servidor (TPS).
- **Players**: Lista de jogadores online com opções de Kick e Broadcast global.
- **Console**: Log em tempo real de chats, entradas/saídas e mensagens do sistema.

## Customização
O frontend foi desenvolvido com **React** e **Lucide-React**. Para alterar as cores ou estilos, edite `client/src/index.css`.
As rotas da API podem ser configuradas no seu `config.yml` do plugin Minecraft.
