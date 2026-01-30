const express = require('express');
const http = require('http');
const { Server } = require('socket.io');
const cors = require('cors');

const app = express();
const server = http.createServer(app);
const io = new Server(server, {
  cors: {
    origin: '*',
  }
});

app.use(cors());
app.use(express.json());

// Global state tracking
let serverStats = {
  players_online: 0,
  max_players: 0,
  tps: 20.0,
  timestamp: Date.now()
};

let players = {};
let logs = [];
let pendingCommands = [];
let lastServerUpdate = Date.now();

// Heartbeat Monitor
setInterval(() => {
  const now = Date.now();
  if (now - lastServerUpdate > 30000 && serverStats.players_online !== -1) {
    console.log('[MONITOR] Server timeout. Cleaning up state.');
    players = {};
    serverStats.players_online = -1;
    io.emit('state_update', { players, serverStats });
  }
}, 5000);

// API Endpoint for Minecraft Plugin
app.post(['/api/events', '/api/event'], (req, res) => {
  const { type, timestamp, data } = req.body;
  if (!type) return res.status(400).send({ status: 'error' });

  lastServerUpdate = Date.now();

  // Process specific events
  switch (type) {
    case 'SERVER_STATS':
      serverStats = { ...data, timestamp };
      break;

    case 'SESSION_START':
      console.log('[MC] New Session Start signal received.');
      players = {};
      logs = [];
      break;

    case 'PLAYER_ACTIVITY':
      if (data.action === 'JOIN') {
        players[data.player] = { player: data.player, joinedAt: timestamp };
        logs.unshift({ type: 'JOIN', player: data.player, timestamp });
      } else if (data.action === 'QUIT') {
        delete players[data.player];
        logs.unshift({ type: 'QUIT', player: data.player, timestamp });
      }
      break;

    case 'CHAT':
      logs.unshift({ type, ...data, timestamp });
      break;

    case 'LOG':
      logs.unshift({ type, ...data, timestamp });
      break;
  }

  if (logs.length > 50) logs.pop();

  io.emit('mc_event', { type, timestamp, data });
  io.emit('state_update', { players, serverStats });

  res.status(200).send({ status: 'ok' });
});

// Admin Actions from Frontend
app.post('/api/admin/action', (req, res) => {
  const { action, target, value } = req.body;
  if (!action) return res.status(400).json({ status: 'error', message: 'Missing action' });

  console.log(`[DASHBOARD] Action Queued: ${action} -> ${target} (${value})`);
  pendingCommands.push({ action, target, value, timestamp: Date.now() });
  res.json({ status: 'queued', action, target });
});

// Polling for Plugin
app.get('/api/admin/poll', (req, res) => {
  const count = pendingCommands.length;
  const cmds = [...pendingCommands];
  if (count > 0) {
    console.log(`[MC POLL] Plugin polling... delivering ${count} commands.`);
    pendingCommands = [];
  }
  res.json(cmds);
});

app.get('/api/status', (req, res) => {
  res.json({
    stats: serverStats,
    onlinePlayers: Object.values(players),
    recentLogs: logs
  });
});

const PORT = 3000;
server.listen(PORT, '0.0.0.0', () => {
  console.log(`Dashboard API running on http://localhost:${PORT}`);
});
