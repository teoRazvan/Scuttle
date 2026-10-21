# Scuttle

CBL Project

<img width="1919" height="1079" alt="image" src="https://github.com/user-attachments/assets/ad245eef-8e8e-4670-825b-305152739a6e" />


## 1. Cloning the repository
If you want to look at the source code, modify it, or build the game yourself, you can clone the repository to your computer.

**Prerequisites:** [Git](https://git-scm.com/downloads) installed on your machine.

1. Open a terminal (Command Prompt, PowerShell, Terminal, etc.).
2. Navigate to the folder where you want to store the project:
```bash
   cd path/to/your/folder
```
3. Clone the repository:
```bash
   git clone https://github.com/teoRazvan/Scuttle.git
```
4. Move into the project folder:
```bash
   cd Scuttle
```

To get the latest changes later on, run the following inside the project folder:
```bash
git pull
```

> **Note:** You don't need to clone the repository just to play the game. See sections 2 and 3 for how to download a ready-made build.

## 2. Building the project
Scuttle is a Java 25 project built with Maven. Builds for **Windows** and **Linux** are produced automatically using GitHub Actions, as defined in [.github/workflows/build.yml](https://github.com/teoRazvan/Scuttle/blob/main/.github/workflows/build.yml). The workflow runs on every push to `main`/`master` and can also be started manually from the **Actions** tab.

The automated workflow performs the following steps:
* **Checkout code**: Retrieves the latest source code from the repository.
* **Setup environment**: Installs Java 25 (Temurin) and caches Maven dependencies.
* **Build**: Compiles and packages the project with `mvn clean package`.
* **Package**: Uses `jpackage` to create a standalone application that includes its own Java runtime, so Java does not need to be installed to play.
* **Upload Artifacts**: Uploads the finished package so it can be downloaded directly from the GitHub interface.

### Downloading a build
1. Go to the **Actions** tab of the repository.
2. Click on the workflow run you want to play (the latest successful one is recommended).
3. Scroll down to **Artifacts** and download the version for your operating system:
   * `Scuttle-Windows` (contains `Scuttle-Windows.zip`)
   * `Scuttle-Linux` (contains `Scuttle-Linux.tar.gz`)

### Building locally (optional)
If you cloned the repository and want to build it yourself, you need [JDK 25](https://adoptium.net/) and [Maven](https://maven.apache.org/download.cgi) installed. From the project folder, run:
```bash
mvn clean package
```
The compiled JAR will be placed in the `target/` folder. You can run it directly with:
```bash
java -jar target/test-1.0-SNAPSHOT.jar
```

## 3. Running the project

### Windows
1. Extract `Scuttle-Windows.zip`.
2. Open the extracted `Scuttle` folder and double-click `Scuttle.exe`.

> **Windows warning:** Because the game is not digitally signed, Windows SmartScreen may show a "Windows protected your PC" message. This is expected. Click **More info**, then **Run anyway** to start the game.

### Linux
1. Extract the archive:
```bash
   tar -xzf Scuttle-Linux.tar.gz
```
2. Run the game:
```bash
   ./Scuttle/bin/Scuttle
```
