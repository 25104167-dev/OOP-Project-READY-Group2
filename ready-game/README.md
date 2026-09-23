# READY: Disaster Preparedness Adventure

A 2D top-down 8-bit pixel-art educational game built in Java (Swing) that
teaches Junior/Senior High School students disaster preparedness,
earthquake safety, environmental awareness, basic first aid, and
evacuation decision-making — in support of **UN Sustainable Development
Goal 4: Quality Education**.

The player explores a school during a developing earthquake, talks to
NPCs, collects information cards, avoids hazards, and completes a short
quiz challenge in each room to earn a key that unlocks the next area. A
full playthrough takes about 15–20 minutes.

No external libraries are used — only the Java standard library
(Swing/AWT) — so there is nothing to download or configure beyond a JDK.

---

## 1. Requirements

- **Java 17 or newer (JDK, not just a JRE)**. The project was built and
  tested against **OpenJDK 21**. You need the *JDK* (which includes
  `javac`), not just a *JRE*, in order to compile from source.
- No internet connection is required to build or run the game — there
  are zero external dependencies.
- Works on Windows, macOS, and Linux (anywhere a desktop Java Swing
  application can run — a normal desktop/laptop, not a headless server).

Check your Java version:

```bash
java -version
javac -version
```

If `javac` is missing, install a JDK:
- **Windows/macOS/Linux**: [Eclipse Temurin](https://adoptium.net/) (recommended, free) or Oracle JDK.
- **Debian/Ubuntu**: `sudo apt install openjdk-21-jdk`
- **macOS (Homebrew)**: `brew install openjdk@21`

---

## 2. Project Structure

```
ready-game/
├── README.md
├── pom.xml                     # optional Maven build file
├── run.sh / run.bat            # one-command build + run scripts
├── dist/
│   └── READY-DisasterPreparedness.jar   # pre-built runnable JAR
└── src/
    ├── main/java/game/
    │   ├── core/        Game, GamePanel, GameManager, GameState
    │   ├── player/      Player, PlayerStats, Inventory
    │   ├── world/       World, Room, RoomFactory, Tile, Checkpoint
    │   ├── objects/     GameObject, Character, InteractiveObject,
    │   │                NPC, Door, Collectible, Hazard, QuizStation,
    │   │                Interactable, Damageable (interfaces)
    │   ├── education/   Question, Quiz, QuizManager, InformationCard,
    │   │                EducationalTopic, LearningProgress
    │   ├── gameplay/    Key, Keys, LifeSystem, ScoreSystem
    │   ├── ui/          HUD, MainMenuScreen, InstructionsScreen,
    │   │                DialogueBox, QuizUI, InventoryUI, PauseScreen,
    │   │                GameOverScreen, VictoryScreen
    │   └── utils/       Constants, InputHandler
    └── test/java/game/
        └── SmokeTest.java   # headless end-to-end logic test (no GUI needed)
```

### OOP concepts, mapped to code

| Concept | Where |
|---|---|
| **Encapsulation** | All fields in `GameObject`, `Player`, `Room`, `Question`, etc. are `private`/`protected` with controlled getters/setters. |
| **Inheritance** | `GameObject` → `Character` → `Player` / `NPC`; `GameObject` → `InteractiveObject` → `Door` / `Collectible` / `Hazard` / `QuizStation`. |
| **Polymorphism** | Every room object is stored as `GameObject`/`Interactable`; `GameManager.handleInteract()` calls `interact()` without knowing the concrete type — a `Door` unlocks a room, an `NPC` opens dialogue, a `Collectible` adds an item, a `QuizStation` starts a quiz. |
| **Abstraction** | `GameObject` and `Character`/`InteractiveObject` are `abstract`; `Interactable` and `Damageable` are interfaces implemented only where meaningful. |

---

## 3. Running It Locally (development / classroom use)

You have three options. Pick whichever is most convenient.

### Option A — One-command script (easiest)

**macOS / Linux:**
```bash
cd ready-game
./run.sh
```

**Windows:**
```bat
cd ready-game
run.bat
```

This compiles the source into an `out/` folder (only if it hasn't been
compiled yet) and launches the game. Re-run the script any time you
change the source — it recompiles automatically if `out/` is missing;
if you're actively editing code, delete the `out/` folder first (or
just run the manual `javac` command below) so your changes are picked
up.

### Option B — Plain `javac` / `java` (no build tool needed)

From the `ready-game/` directory:

```bash
# Compile
mkdir -p out
javac -d out $(find src/main/java -name "*.java")   # macOS/Linux
# Windows (PowerShell):
#   mkdir out
#   Get-ChildItem -Recurse -Filter *.java src\main\java | ForEach-Object { $_.FullName } > sources.txt
#   javac -d out "@sources.txt"

# Run
java -cp out game.core.Game
```

### Option C — Run the pre-built JAR directly

A ready-to-run JAR is included at `dist/READY-DisasterPreparedness.jar`.
No compilation needed:

```bash
java -jar dist/READY-DisasterPreparedness.jar
```

### Option D — Maven (if you use an IDE like IntelliJ / Eclipse / VS Code)

Import `pom.xml` as a Maven project, or from the command line:

```bash
mvn compile exec:java -Dexec.mainClass=game.core.Game
# or build a runnable jar:
mvn package
java -jar target/READY-DisasterPreparedness.jar
```

(Maven's `exec` plugin isn't declared in `pom.xml` to keep the build
dependency-free; if `mvn exec:java` isn't available, just use
`mvn package` and run the resulting jar as shown, or use Option A/B.)

### Controls

| Key | Action |
|---|---|
| `W A S D` or Arrow Keys | Move |
| `E` | Interact (talk / open door / pick up item / start challenge) |
| `I` | Open/close the Knowledge Menu |
| `1`–`4` or `W`/`S` + `Enter` | Answer a quiz question |
| `Enter` | Confirm / advance dialogue |
| `Esc` | Pause / back |

### Running the headless logic test (optional, for developers)

There's a small end-to-end smoke test that exercises the full
quiz → key → door-unlock → room-transition flow **without opening a
window**, useful for CI or quick verification after code changes:

```bash
mkdir -p out out-test
javac -d out $(find src/main/java -name "*.java")
javac -d out-test -cp out src/test/java/game/SmokeTest.java
java -cp out:out-test game.SmokeTest        # macOS/Linux
# java -cp "out;out-test" game.SmokeTest    # Windows
```

It should print a series of `[PASS]` lines ending in
`ALL SMOKE TESTS PASSED`.

---

## 4. "Production" — Packaging and Distributing the Game

For a student project, "production" means producing a build you can
hand to a classmate, teacher, or judge to run **without them installing
your dev environment or fiddling with the classpath**. There's no
server or backend — this is a standalone desktop app, so "deploying to
production" means **packaging a distributable build**, not hosting
anything online. Three tiers, from simplest to most polished:

### Tier 1 — Distribute the runnable JAR (simplest, recommended for submission)

This is usually all you need for a class project:

```bash
mkdir -p out
javac -d out $(find src/main/java -name "*.java")
cd out
jar --create --file ../READY-DisasterPreparedness.jar --main-class game.core.Game .
cd ..
```

Anyone with a JDK **or even just a JRE** (a JAR runtime doesn't need
`javac`) can then run:

```bash
java -jar READY-DisasterPreparedness.jar
```

Distribute the single `.jar` file (plus this README). That's the whole
deliverable — no installer, no setup steps for the recipient beyond
"have Java installed."

If you're using Maven instead, `mvn package` produces the same kind of
jar at `target/READY-DisasterPreparedness.jar` automatically (the
`maven-jar-plugin` in `pom.xml` is already configured with the correct
main class).

### Tier 2 — Native installer / standalone app image (no Java required by the end user)

If your audience shouldn't need Java installed at all, use `jpackage`
(bundled with the JDK since Java 14) to produce a native installer that
bundles a private Java runtime with the app:

```bash
# Build the jar first (Tier 1), then:
jpackage \
  --input dist \
  --main-jar READY-DisasterPreparedness.jar \
  --main-class game.core.Game \
  --name READY \
  --app-version 1.0.0 \
  --vendor "Your Team Name" \
  --description "Disaster Preparedness Adventure - a disaster-preparedness educational game" \
  --type app-image
```

- `--type app-image` produces a self-contained folder you can zip and
  share (works on the OS you built it on — `jpackage` doesn't
  cross-compile, so build on Windows for a Windows app, macOS for a
  macOS app, etc.).
- Swap `--type app-image` for `--type msi` or `--type exe` on Windows,
  `--type dmg` or `--type pkg` on macOS, or `--type deb` / `--type rpm`
  on Linux, to produce a proper double-click installer instead of a
  raw folder. (Windows `.exe`/`.msi` builds require WiX Toolset
  installed; macOS `.dmg` builds work out of the box.)

Run `jpackage --help` for the full list of options (icon, app
description, etc.).

### Tier 3 — Full release checklist

If you want this to feel like a "real" shipped release (e.g. for a
competition submission or portfolio piece):

1. **Version it.** Bump `<version>` in `pom.xml` and the `--app-version`
   passed to `jpackage` for each release.
2. **Test the exact artifact you'll hand out**, not just `java -cp out`.
   Run the actual jar/installer on a clean machine (or at least a
   different folder) before submitting.
3. **Include a short PLAYING.md or in-game instructions** — this game
   already has an in-game Instructions screen (`Instructions` on the
   main menu), so a separate document isn't strictly required, but a
   one-page "how to run + how to play" handout is a nice touch for
   judges/teachers who won't read this full README.
4. **Zip the deliverable**: for Tier 1, zip the `.jar` + a copy of this
   README's "Running It Locally" section; for Tier 2, zip the
   `app-image` folder (or ship the installer file directly).
5. **Don't ship the `out/` build folder or `src/test/`** in your final
   submission zip — only `dist/*.jar` (or the `jpackage` output) plus
   source, if source is required for grading.

### A note on "production" in the traditional sense

This is a self-contained offline desktop application with no network
calls, no database, and no server component — there is no environment
to "deploy to" the way there would be for a web app. If a future
version needs to save progress between sessions, the recommended path
is a simple local save file (see `Constants.SAVE_FILE`, already
reserved as a constant for this) written to the user's home directory
using `java.io.File`/`Properties` or JSON — still fully local, still no
server needed.

---

## 5. Troubleshooting

- **`javac: command not found`** — You have a JRE but not a JDK. Install
  a full JDK (see Requirements above).
- **Game window doesn't appear / `HeadlessException`** — You're running
  in a headless environment (e.g. SSH without X forwarding, a CI
  container, or WSL without a display). This game needs an actual
  desktop/display to run — it's a Swing GUI application, not a
  console/server app. On Linux servers, use a real desktop session or
  X forwarding; there's no headless mode.
- **Blurry / tiny window on a high-DPI display** — this is a fixed-size
  pixel-art window by design (640×512 logical pixels); OS-level display
  scaling may enlarge it, which is expected for this style of game.
- **Nothing happens when I press keys** — click on the game window
  first so it has keyboard focus (this is standard for any desktop
  app, not specific to this game).

---

## 6. Gameplay Summary (for quick reference)

1. **School Entrance** — learn the basics of emergency preparedness.
2. **Classroom** — learn Drop, Cover, and Hold On during an earthquake.
3. **Environmental Awareness Room** — connect environmental care to
   disaster risk (e.g. blocked drainage worsening floods).
4. **First Aid Room** — practice basic first-aid decision-making.
5. **Evacuation Area** — choose the safer marked evacuation route.
6. **Emergency Assembly Area (Final Challenge)** — apply everything
   learned across five final scenario questions.

You have **3 lives**. Touching a hazard or answering a quiz question
incorrectly costs a life; losing all 3 sends you to a Game Over screen
with a short explanation of what went wrong, after which you respawn
at your last checkpoint (the start of your current room) rather than
restarting the whole game.
