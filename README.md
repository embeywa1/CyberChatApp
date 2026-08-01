
# Cyber Dark Terminal Online Chat Application

A high-performance, multi-threaded Java Client-Server terminal chat application built using standard TCP Sockets. Features a custom **Cyber Dark Terminal** theme with glowing RGB ANSI visual effects, real-time message broadcasting, and dynamic thread management.

---

## Table of Contents
- [Architecture & Implementation Details](#architecture--implementation-details)
- [Project Structure](#project-structure)
- [Prerequisites & Requirements](#prerequisites--requirements)
- [How to Build and Run](#how-to-build-and-run)
- [Testing & Execution Guide](#testing--execution-guide)
- [Text-Based User Interface Preview](#text-based-user-interface-preview)
- [Stopping the Application & Port Cleanup](#stopping-the-application--port-cleanup)

---

## Architecture & Implementation Details

The application follows a classic **Client-Server Architecture** using standard Java `java.net` socket APIs and multi-threading primitives.

### 1. `ChatServer.java` (Server Hub)
- **Socket Management:** Listens on port `8080` using `ServerSocket` for incoming client connections.
- **Concurrency & Thread Pooling:** Utilizes `ExecutorService` (`Executors.newCachedThreadPool()`) to assign each client to an independent thread worker upon connection.
- **Client Tracking & Identification:** Maintains a thread-safe `Map<Integer, ClientHandler>` synchronized container. Uses `AtomicInteger` starting at `1001` to generate globally unique User IDs (UIDs) for every connected peer.
- **Broadcasting Engine:** Iterates over active `ClientHandler` socket output streams to relay incoming messages to all connected clients concurrently.

### 2. `ClientHandler.java` (Worker Thread)
- Runs on a dedicated thread per client instance.
- Reads client stream input via `BufferedReader` wrapped around `socket.getInputStream()`.
- Distributes outgoing server payloads using `PrintWriter` wrapped around `socket.getOutputStream()`.
- Implements connection lifecycle management, catching socket failures and triggering clean resource releases and system broadcast notifications.

### 3. `ChatClient.java` (Cyber Dark CLI Client)
- Establishes a TCP connection to `localhost:8080` using `java.net.Socket`.
- **Asynchronous Stream Reading:** Spawns a background listener thread to handle incoming broadcast payloads continuously without blocking user input on the main thread.
- **Cyber Dark Aesthetic UI:** Incorporates 24-bit TrueColor ANSI escape codes (`\u001B[38;2;R;G;Bm`) to render glowing neon green, cyan, and purple terminal themes.

---

## Project Structure

```text
CyberChatApp/
├── src/
│   ├── ChatServer.java      # Multi-threaded TCP Server & Broadcast Engine
│   ├── ClientHandler.java   # Dedicated Client Thread Worker
│   └── ChatClient.java      # Cyber Dark Terminal Visual UI Client
├── bin/                     # Compiled bytecode files (.class)
├── README.md                # Submission & Execution Documentation
└── .gitignore               # Excludes binary output directory from version control
```
##Prerequisites & Requirements
Java Development Kit (JDK): Version 8 or higher (Tested on OpenJDK 17+).

Operating System: Linux, macOS, or Windows terminal environment (Windows Console requires ANSI color support enabled).

Dependencies: None (Uses pure standard Java core libraries: java.net, java.io, java.util, java.util.concurrent).

##How to Build and Run
1. Compilation
From the root directory of the project (CyberChatApp/), execute:
```
bash

javac -d bin src/*.java
```

2. Launching the Server
Start the central ChatServer first in your main terminal window:
```
Bash
java -cp bin ChatServer
Expected Terminal Output:
```
```
text
[CYBER-SERVER] Booting Network Hub on Port 8080...
[CYBER-SERVER] Server Online. Awaiting incoming connections...
```

3. Launching Clients
Open two or more separate terminal windows to simulate multiple users chatting across the network:
```

Bash
java -cp bin ChatClient
```

##Testing & Execution Guide
To test real-time broadcasting across multiple users:

Start ChatServer in Terminal 1.

Start ChatClient in Terminal 2, enter handle NetRunner.

Start ChatClient in Terminal 3, enter handle NeonVibe.

Type messages in Terminal 2 or Terminal 3 to observe live, instant cross-terminal broadcasting.

##Text-Based User Interface Preview
Below is an ASCII representation of the live visual interface:

```
text
======================================================================
   ______   ______  ______  ______  ______   ______  __  __   ______  
  / __  \ / __  \/ __  \/ ____ \/ __  \ / __  \/ / / /  / __  \ 
 / /_/ // /_/ // /_/ // /__/ // /_/ // /_/ // /_/ /  / /_/ / 
 \____/ \____//_/\__/ \_____//_/\__/ \____/ \____/   \____/  
              CYBER DARK GLOW TERMINAL CHAT SYSTEM v1.0              
======================================================================
>> DEFINE YOUR CYBER HANDLE: NetRunner
[+] Authenticated as NetRunner [UID #1001]. Ready to broadcast!

====================== LIVE STREAM START ======================
[SYSTEM] NetRunner [ID #1001] synchronized to the grid.
[SYSTEM] NeonVibe [ID #1002] synchronized to the grid.
[#1002 NeonVibe]: Socket connection verified! Receiving loud and clear.
[#1001 NetRunner]: Broadcast test successful.
```

##Stopping the Application & Port Cleanup
1. Stopping a Client
In the client terminal window, type:

```
Bash
/exit
```

This gracefully closes the client socket, removes the user from the active connection pool, and alerts other connected users.

2. Stopping the Server
In the server terminal window, press Ctrl + C to issue a process interrupt (SIGINT) and close the server socket.

3. Port Cleanup (Troubleshooting)
If the server is shut down unexpectedly and leaves port 8080 locked, release it using:

```
Bash
fuser -k 8080/tcp
```

