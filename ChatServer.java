import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ChatServer acts as the central hub managing socket connections from multiple clients.
 */
public class ChatServer {
    private static final int PORT = 8080;
    
    // Thread-safe collection to track all connected client streams
    private static final Map<Integer, ClientHandler> clients = Collections.synchronizedMap(new HashMap<>());
    private static final AtomicInteger idCounter = new AtomicInteger(1001); // Unique client ID auto-incrementer

    public static void main(String[] args) {
        System.out.println("\u001B[38;2;189;0;255m[CYBER-SERVER] Booting Network Hub on Port " + PORT + "...\u001B[0m");

        ExecutorService threadPool = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("\u001B[38;2;57;255;20m[CYBER-SERVER] Server Online. Awaiting incoming connections...\u001B[0m");

            while (true) {
                Socket socket = serverSocket.accept();
                int clientId = idCounter.getAndIncrement();

                System.out.println("\u001B[38;2;0;255;255m[+] Node Connection Established. UID: #" + clientId + "\u001B[0m");

                ClientHandler clientHandler = new ClientHandler(socket, clientId);
                clients.put(clientId, clientHandler);
                threadPool.execute(clientHandler);
            }
        } catch (IOException e) {
            System.err.println("[SERVER ERROR] Sockets failure: " + e.getMessage());
        } finally {
            threadPool.shutdown();
        }
    }

    /**
     * Broadcasts a message to all active network clients.
     */
    public static void broadcast(String message, int senderId) {
        synchronized (clients) {
            for (Map.Entry<Integer, ClientHandler> entry : clients.entrySet()) {
                entry.getValue().sendMessage(message);
            }
        }
    }

    /**
     * Unregisters a disconnected client and notifies remaining nodes.
     */
    public static void removeClient(int clientId) {
        clients.remove(clientId);
        System.out.println("\u001B[31m[-] Node #" + clientId + " disconnected.\u001B[0m");
        broadcast("\u001B[38;2;255;0;100m[SYSTEM] Client #" + clientId + " dropped off the network.\u001B[0m", 0);
    }
}
