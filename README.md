# SpazioStaff 🛡️

**SpazioStaff** es un plugin de Staff System y Command Spy completo, optimizado y altamente configurable para servidores de Minecraft **Spigot / Paper (1.20 - 1.21+)**.

Inspirado en los mejores sistemas de administración como *AquaCore*, ofrece herramientas avanzadas para la supervisión y control de jugadores en tiempo real.

---

## 🌟 Características Principales

- 🛠️ **Modo Staff (`/mod`, `/staffmode`, `/staff`)**:
  - Entra en modo administración con un solo comando.
  - Guarda tu inventario completo, armadura, nivel de experiencia, modo de juego y estado de vuelo al activar Staff Mode, y lo restaura de forma intacta al salir.
  - Otorga una hotbar personalizada de ítems administrativos.

- 👻 **Sistema de Vanish (`/vanish`, `/v`)**:
  - Hazte completamente invisible para los jugadores normales.
  - Los miembros del Staff con permiso pueden ver a otros staffs en Vanish.
  - Totalmente configurable desde la `config.yml`.

- 🧊 **Sistema de Freeze (`/freeze`, `/unfreeze`, `/ss`)**:
  - Congela a jugadores sospechosos de usar trampas (hacks/cheats).
  - Bloquea movimiento, ataques, recibir daño, romper/colocar bloques, tirar o recoger ítems e interactuar con inventarios.
  - Muestra un mensaje y título de advertencia en pantalla al jugador congelado ("*Si te desconectas serás sancionado*").
  - **Alerta de Desconexión**: Notifica a todo el staff si un jugador congelado abandona el servidor durante la revisión.

- 🧭 **Brújula Phase**:
  - Telepórtate o traspasa paredes, bloques y estructuras en la dirección que estás mirando con un solo click.

- 🔍 **Inspeccionador de Inventario en Vivo (GUI)**:
  - Visualiza el inventario completo de cualquier jugador en tiempo real.
  - Muestra la armadura equipada (Casco, Pechera, Pantalones, Botas), ítem en la mano secundaria y un resumen con su Vida, Comida, Ping, Modo de juego y Coordenadas.

- 📊 **Menú de Stats de Jugador (GUI)**:
  - Inspecciona las estadísticas completas de un jugador (Vida, Comida, Ping, Modo de juego, Mundo, Coordenadas, Dirección IP y estado de Freeze).
  - Botones de acción rápida: **Teletransportarse al jugador**, **Inspeccionar Inventario** y **Congelar / Descongelar**.

- 👥 **Menú de Staff y Jugadores Online (GUI)**:
  - Lista interactiva con las cabezas de textura (`PLAYER_HEAD`) de todos los miembros del Staff y usuarios conectados.
  - Al hacer click en la cabeza de cualquier jugador se abre directamente su menú de estadísticas con la opción de teletransportarte hacia él.

- 🦅 **Teleport Aleatorio (Random TP)**:
  - Ítem de pluma que te teletransporta instantáneamente a un jugador en línea no-staff al azar.

- 🕵️ **Command Spy (`/cmdspy`, `/cspy`)**:
  - Monitorea los comandos ejecutados por los jugadores en tiempo real.
  - Filtro de comandos ignorados (ej. `/login`, `/register`, `/pass`) para proteger contraseñas y privacidad.

---

## 📜 Comandos y Permisos

| Comando | Aliases | Descripción | Permiso | Por defecto |
| :--- | :--- | :--- | :--- | :--- |
| `/cmdspy [on\|off]` | `/cspy`, `/commandspy` | Activa o desactiva la observación de comandos. | `spaziostaff.cmdspy` | OP |
| `/staffmode [jugador]` | `/mod`, `/staff` | Entra o sale del Modo Staff (o altera el de otro). | `spaziostaff.staffmode` | OP |
| `/vanish [on\|off]` | `/v` | Activa o desactiva la invisibilidad en el servidor. | `spaziostaff.vanish` | OP |
| `/freeze <jugador>` | `/ss`, `/freezear` | Congela a un jugador para revisión de hacks. | `spaziostaff.freeze` | OP |
| `/unfreeze <jugador>`| `/unss`, `/desfreezear` | Descongela a un jugador previamente sancionado. | `spaziostaff.freeze` | OP |

---

## ⚙️ Configuración (`config.yml`)

```yaml
# Configuración General de SpazioStaff
prefix: "&7[&bSpazioStaff&7] "

messages:
  no-permission: "&cNo tienes permisos para usar este comando."
  only-players: "&cEste comando solo puede ser ejecutado por jugadores."
  player-not-found: "&cJugador no encontrado o desconectado."

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
  - "/l"
  - "/reg"

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

# Staff Mode Items & Setup
staffmode:
  enabled: "&aModo Staff ACTIVADO."
  disabled: "&cModo Staff DESACTIVADO."
  
  items:
    vanish-on:
      slot: 0
      material: "LIME_DYE"
      name: "&a&lVanish: ACTIVADO &7(Click derecho)"
      lore:
        - "&7Click derecho para hacerte visible."
    vanish-off:
      slot: 0
      material: "GRAY_DYE"
      name: "&c&lVanish: DESACTIVADO &7(Click derecho)"
      lore:
        - "&7Click derecho para hacerte invisible."
    
    freeze:
      slot: 1
      material: "PACKED_ICE"
      name: "&b&lCongelar Jugador &7(Click en Jugador)"
      lore:
        - "&7Click en un jugador para congelarlo o descongelarlo."

    phase:
      slot: 2
      material: "COMPASS"
      name: "&e&lBrújula Phase &7(Click derecho/izquierdo)"
      lore:
        - "&7Click para traspasar bloques/paredes."

    inspect:
      slot: 4
      material: "BOOK"
      name: "&6&lInspeccionar Inventario &7(Click en Jugador)"
      lore:
        - "&7Click en un jugador para ver su inventario y armadura."

    stats:
      slot: 6
      material: "NETHER_STAR"
      name: "&d&lStats del Jugador &7(Click en Jugador)"
      lore:
        - "&7Click en un jugador para ver sus estadísticas."

    online-list:
      slot: 7
      material: "PLAYER_HEAD"
      name: "&3&lLista de Staff y Jugadores &7(Click derecho)"
      lore:
        - "&7Abre el menú de jugadores y staff online."

    random-tp:
      slot: 8
      material: "FEATHER"
      name: "&9&lTeleport Aleatorio &7(Click derecho)"
      lore:
        - "&7Telepórtate a un jugador aleatorio."

# Configuración de GUIs
gui:
  inspect-title: "&8Inspeccionando: &1{player}"
  online-list-title: "&8Jugadores & Staff Online"
  stats-title: "&8Stats de: &1{player}"
```

---

## 🏗️ Compilación e Instalación

### Requisitos:
- Java 17 o superior.
- Servidor Spigot / Paper 1.20 o superior.

### Pasos:
1. Descarga el archivo `.jar` compilado desde la sección de **[Releases](https://github.com/gallikidog/spaziostaff/releases)**.
2. Coloca el archivo `SpazioStaff-1.0.0.jar` en la carpeta `/plugins/` de tu servidor.
3. Inicia o reinicia el servidor.
4. ¡Disfruta de la mejor experiencia administrativa en tu servidor!
