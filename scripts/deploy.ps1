Param(
    [string]$PluginsDir = "C:\Users\FoxOS_User\Downloads\server\plugins"
)

$ErrorActionPreference = "Stop"


Write-Host "Checking for running server..."
$JavaProcess = Get-CimInstance Win32_Process -Filter "Name = 'java.exe'" | Where-Object { $_.CommandLine -like "*spigot*" -or $_.CommandLine -like "*paper*" -or $_.CommandLine -like "*server*" }

if ($JavaProcess) {
    Write-Warning "⚠️  SERVER APPEARS TO BE RUNNING!"
    Write-Warning "Deploying while the server is running usually fails to update the JAR file due to file locking."
    Write-Warning "Please STOP the server, then run this script again."
    # We won't exit, but we warn heavily.
    Start-Sleep -Seconds 3
}

$MvnCmd = "C:\Users\FoxOS_User\Desktop\apache-maven-3.9.12\bin\mvn.cmd"

Write-Host "Building Plugin..."
& $MvnCmd clean package

if ($LASTEXITCODE -ne 0) {
    Write-Error "Build failed."
    exit 1
}

$TargetDir = "target"
$JarFile = Get-ChildItem -Path $TargetDir -Filter "*.jar" | Where-Object { $_.Name -notlike "original-*" } | Select-Object -First 1

if (-not $JarFile) {
    Write-Error "No JAR file found in target directory."
    exit 1
}

Write-Host "Deploying $($JarFile.Name) to $PluginsDir..."

if (-not (Test-Path $PluginsDir)) {
    Write-Warning "Plugins directory '$PluginsDir' does not exist. Creating it..."
    New-Item -ItemType Directory -Force -Path $PluginsDir | Out-Null
}

Copy-Item -Path $JarFile.FullName -Destination "$PluginsDir\$($JarFile.Name)" -Force

Write-Host "Deployment Complete!" -ForegroundColor Green
