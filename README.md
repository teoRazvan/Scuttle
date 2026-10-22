# Scuttle

An endless underwater arcade game made in Java Swing: guide a crab across procedurally generated lanes of coral, fish, jellyfish and torpedoes.

CBL project for 2IP90 Programming, TU/e, 2026–2027, by Teo Ene and Andrei ____.

<img width="1919" height="1079" alt="Scuttle gameplay" src="https://github.com/user-attachments/assets/ad245eef-8e8e-4670-825b-305152739a6e" />

## Contents
1. [Quick start](#1-quick-start)
2. [Running from source code](#2-running-from-source-code)
3. [Downloading a ready-made build](#3-downloading-a-ready-made-build)
4. [Controls and how to play](#4-controls-and-how-to-play)
5. [Running the tests](#5-running-the-tests)
6. [Project structure](#6-project-structure)
7. [Credits](#7-credits)
8. [Known limitations](#8-known-limitations)

## 1. Quick start
**Requirement:** [JDK 25](https://adoptium.net/) (Windows, macOS or Linux).

The fastest way to play from the submitted ZIP or a cloned repository:

1. Open the `Scuttle` folder in **NetBeans** (*File → Open Project*).
2. Click **Run Project** (F6).

The game opens on the main menu. Click **PLAY** to start.

No IDE? See [Command line](#command-line) below.

## 2. Running from source code
The project is a standard Maven project. The main class is `scuttle.view.Scuttle`.

**Dependencies:** the game itself only uses the Java standard library (Swing and AWT), so it needs nothing besides JDK 25 to run. The only external dependency is **JUnit 5** (with `opentest4j`), used for the tests and declared in `pom.xml`. Maven, or the Maven support built into your IDE, downloads it automatically on the first build, so the first build needs an internet connection.

### NetBeans
1. *File → Open Project*, and select the `Scuttle` folder (it shows a Maven icon).
2. Make sure the project uses JDK 25: right-click the project → *Properties → Build → Compile → Java Platform*. If JDK 25 is not listed, add it under *Tools → Java Platforms*.
3. Click **Run Project** (F6).

### IntelliJ IDEA
1. *File → Open*, select the `Scuttle` folder, and choose **Open as Project**. Click **Trust Project** if asked.
2. Set the JDK: *File → Project Structure → Project → SDK* → choose JDK 25 (or use *Download JDK*).
3. Open `src/main/java/scuttle/view/Scuttle.java` and click the green **Run** arrow next to `main`.

### Visual Studio Code
1. Install the **Extension Pack for Java** (by Microsoft).
2. *File → Open Folder*, and select the `Scuttle` folder. Wait until the Java projects finish loading.
3. If needed, select JDK 25 with the command *Java: Configure Java Runtime*.
4. Open `src/main/java/scuttle/view/Scuttle.java` and click **Run** above `main`.

### Command line
You need [JDK 25](https://adoptium.net/) and [Maven](https://maven.apache.org/download.cgi). From the project folder, run:
```bash
mvn clean package
java -jar target/scuttle-1.0-SNAPSHOT.jar
```
`mvn clean package` also runs all tests. To skip them for a faster build, add `-DskipTests`.

### Cloning the repository
If you don't have the source code yet:
```bash
git clone https://github.com/teoRazvan/Scuttle.git
cd Scuttle
```
To get the latest changes later, run `git pull` inside the project folder.

## 3. Downloading a ready-made build
Standalone builds for **Windows**, **macOS** and **Linux** are produced automatically with GitHub Actions, as defined in [.github/workflows/build.yml](https://github.com/teoRazvan/Scuttle/blob/main/.github/workflows/build.yml). They include their own Java runtime, so Java does not need to be installed to play.

The workflow runs on every push to `main`/`master` and can also be started manually from the **Actions** tab. It:
* **Checks out** the latest source code.
* **Sets up** Java 25 (Temurin) and caches Maven dependencies.
* **Builds** the project with `mvn clean package`.
* **Packages** a standalone application with `jpackage`.
* **Uploads** the packages so they can be downloaded from GitHub.

### Downloading
1. Go to the **Actions** tab of the repository.
2. Click the latest successful workflow run.
3. Scroll down to **Artifacts** and download the version for your operating system:
   * `Scuttle-Windows` (contains `Scuttle-Windows.zip`)
   * `Scuttle-macOS` (contains `Scuttle-macOS.zip`)
   * `Scuttle-Linux` (contains `Scuttle-Linux.tar.gz`)

> **Note:** GitHub deletes these artifacts after 90 days. Running from source (section 2) always works.

### Windows
1. Extract `Scuttle-Windows.zip`.
2. Open the extracted `Scuttle` folder and double-click `Scuttle.exe`.

> **Windows warning:** the game is not digitally signed, so Windows SmartScreen may show "Windows protected your PC". Click **More info**, then **Run anyway**.

### macOS
1. Extract `Scuttle-macOS.zip`.
2. Double-click `Scuttle.app`.

> **macOS warning:** the app is not signed, so macOS may say it "could not verify" Scuttle. Click **Done**, then go to *System Settings → Privacy & Security*, scroll down and click **Open Anyway**. This build runs on Apple Silicon Macs (M1 and newer). On an Intel Mac, run the game from source (section 2).

### Linux
1. Extract the archive:
```bash
tar -xzf Scuttle-Linux.tar.gz
```
2. Run the game:
```bash
./Scuttle/bin/Scuttle
```

## 4. Controls and how to play
| Key | Action |
| --- | --- |
| Arrow keys / WASD | Hop one cell |
| Esc or the pause button | Pause and resume |
| R | Restart after game over |
| F11 | Fullscreen |
| F10 | Leave fullscreen |

Move up as far as you can. Every new row you reach adds a point to your score.

* **Coral** and the **torpedo launcher** block your way.
* **Fish** kill you if they swim into you.
* **Trenches** can only be crossed by riding the **jellyfish**. Don't fall in, and don't ride off the edge of the screen.
* **Torpedo lanes** blink a warning before a water jet fires across the whole lane.
* Collect **coins** and spend them in the **shop** on one of 10 crab skins.

The game gets harder the further you go. Your high score, coins and skins are saved between sessions.

## 5. Running the tests
The project has 171 JUnit 5 tests.

* **NetBeans:** right-click the project → **Test** (Alt+F6).
* **IntelliJ IDEA:** right-click `src/test/java` → **Run 'All Tests'**.
* **VS Code:** open the **Testing** tab and click **Run Tests**.
* **Command line:** `mvn test`

## 6. Project structure
The code follows the Model–View–Controller pattern:

| Package | Contents |
| --- | --- |
| `scuttle.model` | The game world and rules: lanes, the player, movers, the lane generator (`LaneGenerator`), the BFS path check (`Reachability`), difficulty values (`GameRules`) and saved data. No Swing code. |
| `scuttle.controller` | `GameController`: game loop ticks, pause, restart and saving results. |
| `scuttle.view` | The Swing window, menus, shop and all rendering. The entry point is `Scuttle.java`. |

Tests are in `src/test/java`, and images and fonts are in `src/main/resources`.

Our two advanced topics are **procedural generation with BFS validation** and **unit testing with JUnit**. See the reflection PDF for details.

## 7. Credits
* **Font:** "Press Start 2P" by CodeMan38, licensed under the SIL Open Font License 1.1 (licence included in `src/main/resources/fonts/`).
* **Graphics:** the pixel-art sprites were generated with AI, as approved by our tutor at the start of the project. All code was written by us.

## 8. Known limitations
* On 4:3 screens the game is slightly stretched.
* The packaged apps are not signed, so Windows and macOS show a warning the first time (see section 3).
* The macOS package only runs on Apple Silicon. Intel Macs can run the game from source.