# Build script for SpazioStaff Plugin
$ErrorActionPreference = "Stop"

Write-Host "===> Compilando SpazioStaff..." -ForegroundColor Cyan

$spigotJar = "$env:USERPROFILE\.m2\repository\org\spigotmc\spigot-api\1.21-R0.1-SNAPSHOT\spigot-api-1.21-R0.1-SNAPSHOT.jar"

if (-not (Test-Path $spigotJar)) {
    # Buscar cualquier spigot-api jar disponible en .m2
    $spigotJarObj = Get-ChildItem -Path "$env:USERPROFILE\.m2\repository\org\spigotmc\spigot-api" -Recurse -Filter "*.jar" | Select-Object -First 1
    if ($spigotJarObj) {
        $spigotJar = $spigotJarObj.FullName
    } else {
        Write-Host "No se encontro spigot-api.jar localmente en .m2, descargando desde SpigotMC repo..." -ForegroundColor Yellow
        $spigotJar = "$PSScriptRoot\lib\spigot-api-1.21.jar"
        if (-not (Test-Path $spigotJar)) {
            New-Item -ItemType Directory -Path "$PSScriptRoot\lib" -Force | Out-Null
            $url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/org/spigotmc/spigot-api/1.21-R0.1-SNAPSHOT/spigot-api-1.21-R0.1-20240807.214924-85.jar"
            Invoke-WebRequest -Uri $url -OutFile $spigotJar
        }
    }
}

Write-Host "Utilizando Spigot API desde: $spigotJar" -ForegroundColor Green

# Preparar carpetas
$buildDir = "$PSScriptRoot\target\classes"
$outJar = "$PSScriptRoot\target\SpazioStaff-1.0.0.jar"

if (Test-Path "$PSScriptRoot\target") {
    Remove-Item "$PSScriptRoot\target" -Recurse -Force
}
New-Item -ItemType Directory -Path $buildDir -Force | Out-Null

# Obtener todos los archivos Java
$javaFiles = Get-ChildItem -Path "$PSScriptRoot\src\main\java" -Recurse -Filter "*.java" | ForEach-Object { $_.FullName }

# Compilar Java
Write-Host "Compilando fuentes Java..." -ForegroundColor Cyan
& javac -encoding UTF-8 -cp "$spigotJar" -d "$buildDir" $javaFiles

if ($LASTEXITCODE -ne 0) {
    Write-Host "Error en la compilacion!" -ForegroundColor Red
    exit 1
}

# Copiar recursos (plugin.yml, config.yml)
Write-Host "Copiando recursos (plugin.yml, config.yml)..." -ForegroundColor Cyan
Copy-Item "$PSScriptRoot\src\main\resources\*" -Destination "$buildDir" -Recurse -Force

# Crear JAR
Write-Host "Empaquetando JAR en $outJar..." -ForegroundColor Cyan
Push-Location $buildDir
& jar -cf "$outJar" *
Pop-Location

Write-Host "==========================================" -ForegroundColor Green
Write-Host "SpazioStaff compilado exitosamente!" -ForegroundColor Green
Write-Host "Archivo JAR generado en: $outJar" -ForegroundColor Green
Write-Host "==========================================" -ForegroundColor Green

