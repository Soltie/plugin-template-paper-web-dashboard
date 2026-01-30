@echo off
echo Starting Dashboard Backend...
start cmd /k "cd server && npm install && node index.js"
echo Starting Dashboard Frontend...
start cmd /k "cd client && npm install && npm run dev"
echo Dashboard setup finished!
pause
