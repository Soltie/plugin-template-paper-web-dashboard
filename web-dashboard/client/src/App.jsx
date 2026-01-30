import React, { useState, useEffect } from 'react';
import { io } from 'socket.io-client';
import {
  Users,
  Activity,
  MessageSquare,
  Shield,
  Clock,
  Zap,
  Terminal,
  Settings2,
  Send,
  UserX,
  LayoutDashboard,
  LogOut
} from 'lucide-react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

const API_URL = `http://${window.location.hostname}:3000`;
const socket = io(API_URL);

function App() {
  const [stats, setStats] = useState({ players_online: 0, max_players: 0, tps: 20 });
  const [players, setPlayers] = useState([]);
  const [logs, setLogs] = useState([]);
  const [activeTab, setActiveTab] = useState('overview');
  const [history, setHistory] = useState([]);

  useEffect(() => {
    fetch(`${API_URL}/api/status`)
      .then(res => res.json())
      .then(data => {
        setStats(data.stats);
        setPlayers(data.onlinePlayers);
        setLogs(data.recentLogs);
      });

    socket.on('mc_event', (event) => {
      const { type, data } = event;
      if (type === 'SERVER_STATS') {
        setStats(data);
        setHistory(prev => [...prev.slice(-19), { time: new Date().toLocaleTimeString(), players: data.players_online }]);
      }
    });

    socket.on('state_update', (state) => {
      setPlayers(Object.values(state.players));
      setStats(state.serverStats);
    });

    return () => {
      socket.off('mc_event');
      socket.off('state_update');
    };
  }, []);

  const sendAction = (action, target, value = '') => {
    fetch(`${API_URL}/api/admin/action`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ action, target, value })
    });
  };

  return (
    <div className="app-container">
      <aside className="sidebar">
        <div className="logo">
          <LayoutDashboard size={24} className="logo-icon" />
          <h1>TEMPLATE DASH</h1>
        </div>

        <nav>
          <NavItem icon={<Activity size={20} />} label="Overview" active={activeTab === 'overview'} onClick={() => setActiveTab('overview')} />
          <NavItem icon={<Users size={20} />} label="Players" active={activeTab === 'players'} onClick={() => setActiveTab('players')} />
          <NavItem icon={<Terminal size={20} />} label="Console" active={activeTab === 'console'} onClick={() => setActiveTab('console')} />
        </nav>

        <div className="footer-status">
          <div className="status-indicator online"></div>
          <span>Cloud Connect Active</span>
        </div>
      </aside>

      <main className="main-content">
        <header>
          <h2>{activeTab.charAt(0).toUpperCase() + activeTab.slice(1)}</h2>
          <div className="server-info">
            <Clock size={16} />
            <span>{new Date().toLocaleTimeString()}</span>
          </div>
        </header>

        {activeTab === 'overview' && <Overview stats={stats} players={players} history={history} />}
        {activeTab === 'players' && <PlayersView players={players} onAction={sendAction} />}
        {activeTab === 'console' && <ConsoleView logs={logs} onAction={sendAction} />}
      </main>
    </div>
  );
}

function NavItem({ icon, label, active, onClick }) {
  return (
    <div className={`nav-item ${active ? 'active' : ''}`} onClick={onClick}>
      {icon}
      <span>{label}</span>
    </div>
  );
}

function Overview({ stats, players, history }) {
  return (
    <div className="view-container">
      <div className="stats-grid">
        <StatCard icon={<Users />} label="Players" value={`${stats.players_online}/${stats.max_players}`} />
        <StatCard icon={<Zap />} label="TPS" value={stats.tps.toFixed(1)} color={stats.tps > 18 ? '#22c55e' : '#eab308'} />
        <StatCard icon={<Shield />} label="Status" value="Protected" />
        <StatCard icon={<Clock />} label="Health" value="Good" />
      </div>

      <div className="chart-card card">
        <h3>Online Players History</h3>
        <div style={{ height: '300px', width: '100%', marginTop: '1rem' }}>
          <ResponsiveContainer width="99%" height="100%">
            <LineChart data={history}>
              <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
              <XAxis dataKey="time" stroke="#94a3b8" />
              <YAxis stroke="#94a3b8" />
              <Tooltip contentStyle={{ background: '#1e293b', border: '1px solid #334155' }} />
              <Line type="monotone" dataKey="players" stroke="#38bdf8" strokeWidth={2} dot={false} />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
}

function PlayersView({ players, onAction }) {
  const [broadcast, setBroadcast] = useState('');

  return (
    <div className="view-container">
      <div className="action-bar card">
        <input
          type="text"
          placeholder="Broadcast a message..."
          value={broadcast}
          onChange={(e) => setBroadcast(e.target.value)}
        />
        <button onClick={() => { onAction('BROADCAST', 'ALL', broadcast); setBroadcast(''); }}>
          <Send size={16} /> Broadcast
        </button>
      </div>

      <div className="players-list">
        {players.map(p => (
          <div key={p.player} className="player-card card">
            <div className="p-info">
              <h3>{p.player}</h3>
              <span>Joined at {new Date(p.joinedAt).toLocaleTimeString()}</span>
            </div>
            <button className="kick-btn" onClick={() => onAction('KICK', p.player)}>
              <LogOut size={16} /> Kick
            </button>
          </div>
        ))}
        {players.length === 0 && <div className="empty-state">No players online.</div>}
      </div>
    </div>
  );
}

function ConsoleView({ logs }) {
  return (
    <div className="view-container">
      <div className="console-card card">
        {logs.map((log, i) => (
          <div key={i} className={`log-line type-${log.type}`}>
            <span className="timestamp">[{new Date(log.timestamp).toLocaleTimeString()}]</span>
            <span className="type">{log.type}:</span>
            <span className="msg">{log.player ? `${log.player}: ` : ''}{log.message || log.action || ''}</span>
          </div>
        ))}
        {logs.length === 0 && <div className="empty-state">Console is empty.</div>}
      </div>
    </div>
  );
}

function StatCard({ icon, label, value, color }) {
  return (
    <div className="stat-card card">
      <div className="icon" style={{ color: color || '#38bdf8' }}>{icon}</div>
      <div className="details">
        <span className="label">{label}</span>
        <span className="value">{value}</span>
      </div>
    </div>
  );
}

export default App;
