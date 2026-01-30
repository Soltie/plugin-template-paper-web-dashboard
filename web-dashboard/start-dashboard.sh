#!/bin/bash

# Portas padrão
SERVER_PORT=3000
CLIENT_PORT=5173

echo "------------------------------------------"
echo "   GERENCIADOR DE DASHBOARD (TEMPLATE)    "
echo "------------------------------------------"

# Função para encontrar e matar processos em uma porta
kill_port() {
    local port=$1
    local pid=$(lsof -t -i:$port)
    if [ ! -z "$pid" ]; then
        echo "[!] Detectado dashboard rodando na porta $port (PID: $pid). Encerrando..."
        kill -9 $pid
        sleep 1
    else
        echo "[+] Porta $port está livre."
    fi
}

# 1. Limpeza de processos antigos
kill_port $SERVER_PORT
kill_port $CLIENT_PORT

# 2. Inicialização
echo "[*] Iniciando Dashboard..."

# Pega o diretório do script
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Iniciar Backend
echo "[*] Preparando Backend (Server)..."
cd "$DIR/server"
if [ ! -d "node_modules" ]; then
    echo "[*] Instalando dependências do servidor..."
    npm install --silent
fi
nohup node index.js > "$DIR/server.log" 2>&1 &
echo "[+] Backend iniciado em background. (Logs: web-dashboard/server.log)"

# Iniciar Frontend
echo "[*] Preparando Frontend (Client)..."
cd "$DIR/client"
if [ ! -d "node_modules" ]; then
    echo "[*] Instalando dependências do cliente..."
    npm install --silent
fi
nohup npm run dev -- --host 0.0.0.0 > "$DIR/client.log" 2>&1 &
echo "[+] Frontend iniciado em background. (Logs: web-dashboard/client.log)"

echo "------------------------------------------"
echo "[SUCCESS] Dashboard reiniciado com sucesso!"
echo "Backend: http://localhost:$SERVER_PORT"
echo "Frontend: http://localhost:$CLIENT_PORT"
echo "------------------------------------------"
