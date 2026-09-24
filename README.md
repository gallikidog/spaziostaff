# SpazioStaff - Staff System & CommandSpy Core

**SpazioStaff** es un plugin integral de administración y moderación para servidores de Minecraft **Spigot / Paper 1.20 - 1.21+**, diseñado por **MineSpazio Network**. Integra un sistema completo de Staff Mode con ítems interactivos de inspección, Vanish, Freeze/SS, CommandSpy en tiempo real, Scoreboard de métricas para moderadores, e integración de alta prioridad de **StaffChat**.

---

## 🌟 Características Principales

- 🛡️ **Staff Mode (`/staff`)**: Entra en modo moderador conservando tu inventario original intacto. Otorga una barra de herramientas de acceso rápido (Freeze, Inspect GUI, Stats GUI, Phase Compass, Random TP, Online List).
- 💬 **StaffChat (`/sc`, `/staffchat`)**: Canal de chat privado y exclusivo para el equipo de Staff. Soporta formato por prefijos de LuckPerms, sonidos personalizables y toggle de chat. **Sobreescribe con máxima prioridad (`HIGHEST`) cualquier otro plugin que contenga el comando `/sc`**.
- 🕵️ **CommandSpy (`/cmdspy`)**: Visualiza en tiempo real los comandos ejecutados por otros jugadores. Ignora automáticamente comandos sensibles como contraseñas (`/login`, `/register`, `/pass`).
- 👻 **Vanish (`/v`, `/vanish`)**: Invisibilidad completa frente a jugadores normales. Los miembros del staff pueden verse entre sí si está habilitado en la configuración.
- 🧊 **Freeze / SS (`/freeze`, `/unfreeze`)**: Congela a jugadores sospechosos durante revisiones. Bloquea movimiento, comandos, bloqueos e inventarios con mensajes de advertencia configurables.
- 📊 **Scoreboard de Métricas**: Muestra en pantalla el estado del Staff, TPS del servidor, cantidad de staffs online, jugadores conectados y tiempo de sesión activa.
- 🔍 **Inspección de Inventarios y Stats GUIs**: Inspecciona el inventario, armadura, salud, nivel, dirección IP y coordenadas de cualquier jugador mediante menús interactivos.

---

## 📜 Lista Completa de Comandos y Alias

| Comando | Alias | Descripción | Permiso |
| :--- | :--- | :--- | :--- |
| `/staffchat [mensaje]` | `/sc`, `/schat` | Envía un mensaje privado al StaffChat o conmuta el modo automático (toggle). **Antepuesto con máxima prioridad sobre otros plugins**. | `spaziostaff.staffchat` (o `spaziostaff.sc`) |
| `/staffmode [jugador]` | `/mod`, `/staff` | Entra o sale del Modo Staff (guarda/restaura inventario, equipa hotbar de moderación y scoreboard). | `spaziostaff.staffmode` |
| `/vanish [on\|off]` | `/v` | Activa o desactiva el modo invisible frente a jugadores ordinarios. | `spaziostaff.vanish` |
| `/freeze <jugador>` | `/ss`, `/freezear` | Congela a un jugador para revisión, impidiéndole moverse o desconectarse. | `spaziostaff.freeze` |
| `/unfreeze <jugador>` | `/unss`, `/desfreezear` | Descongela a un jugador previamente sancionado/revisado. | `spaziostaff.freeze` |
| `/cmdspy [on\|off]` | `/cspy`, `/commandspy` | Activa o desactiva la supervisión de comandos en tiempo real. | `spaziostaff.cmdspy` |

---

## 🔑 Tabla Completa de Permisos

| Permiso | Descripción | Por Defecto |
| :--- | :--- | :--- |
| `spaziostaff.staffchat` | Permite enviar, recibir y usar el StaffChat (`/sc`). | `OP` |
| `spaziostaff.sc` | Alias alternativo para acceder al StaffChat (`/sc`). | `OP` |
| `spaziostaff.staffmode` | Permite entrar en Modo Staff (`/staff`) y ver el Scoreboard. | `OP` |
| `spaziostaff.vanish` | Permite usar Vanish (`/v`) y ver a otros miembros invisibles. | `OP` |
| `spaziostaff.freeze` | Permite congelar y descongelar jugadores (`/freeze`, `/unfreeze`). | `OP` |
| `spaziostaff.cmdspy` | Permite activar y visualizar el CommandSpy (`/cmdspy`). | `OP` |
| `spaziostaff.admin` | Permiso de administración total sobre todas las funciones del plugin. | `OP` |

---

## 🎒 Barra de Herramientas del Modo Staff (Hotbar Items)

Al activar `/staffmode`, recibes las siguientes herramientas configurables en `config.yml`:

1. **Slot 0 - Vanish Toggle (`LIME_DYE` / `GRAY_DYE`)**: Alterna visibilidad en tiempo real.
2. **Slot 1 - Freeze (`PACKED_ICE`)**: Click en un jugador para congelar/descongelar instantáneamente.
3. **Slot 2 - Phase Compass (`COMPASS`)**: Click para atravesar muros y bloques en la dirección de la mirada.
4. **Slot 4 - Inspect GUI (`BOOK`)**: Click en un jugador para abrir su inventario, armadura y salud en una GUI.
5. **Slot 6 - Stats GUI (`NETHER_STAR`)**: Click en un jugador para consultar sus métricas y dirección IP.
6. **Slot 7 - Online List (`PLAYER_HEAD`)**: Abre un menú con la lista de jugadores y moderadores conectados.
7. **Slot 8 - Random TP (`FEATHER`)**: Teletransporte aleatorio instantáneo a un jugador activo.

---

## ⚙️ Configuración por Defecto (`config.yml`)

```yaml
# Configuración General de SpazioStaff
prefix: "&7[&bSpazioStaff&7] "

messages:
  no-permission: "&cNo tienes permisos para usar este comando."
  only-players: "&cEste comando solo puede ser ejecutado por jugadores."
  player-not-found: "&cJugador no encontrado o desconectado."

# Staff Chat
staffchat:
  format: "&8[&c&lSTAFFCHAT&8] &7{prefix}&f{player}&7: &e{message}"
  toggle-enabled: "&aModo StaffChat &eACTIVADO&a. Todos tus mensajes irán al StaffChat."
  toggle-disabled: "&cModo StaffChat &eDESACTIVADO&c. Tus mensajes volverán al chat público."
  no-permission: "&cNo tienes permisos para usar el StaffChat."
  sound:
    enabled: true
    name: "ENTITY_EXPERIENCE_ORB_PICKUP"
    volume: 1.0
    pitch: 1.0

# Command Spy
cmdspy:
  prefix: "&7[&cCommandSpy&7] "
  enabled: "&aCommand Spy activado correctamente."
  disabled: "&cCommand Spy desactivado correctamente."
  format: "{prefix}&f{player}&7: &e{command}"

ignored-commands:
  - "/cmdspy"
  - "/cspy"
  - "/commandspy"
  - "/login"
  - "/register"
  - "/pass"
  - "/changepassword"

# Vanish
vanish:
  enabled: "&aAhora estas en Vanish (Invisible)."
  disabled: "&cYa no estas en Vanish (Visible)."
  staff-see-vanished: true

# Freeze
freeze:
  frozen-target-msg:
    - "&c&l=========================================="
    - "&c&lHAS SIDO CONGELADO POR UN STAFF"
    - "&eSi te desconectas seras &cSANCIONADO PERMANENTEMENTE&e."
    - "&ePor favor unete a nuestro Discord para revision."
    - "&c&l=========================================="
  unfrozen-target-msg: "&aHas sido descongelado por el staff."
  frozen-staff-notify: "&aHas congelado a &e{player}&a."
  unfrozen-staff-notify: "&aHas descongelado a &e{player}&a."
  cannot-move: "&c&lPROHIBIDO MOVERTE: &cEstas congelado."
```

---

## 🛠️ Compilación e Instalación

1. Requisitos: JDK 17 o superior.
2. Compilar con Maven:
   ```bash
   mvn clean package
   ```
3. El archivo comprimido compilado se generará en:
   `target/SpazioStaff-1.1.1.jar`
