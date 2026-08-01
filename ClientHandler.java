import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * ClientHandler reads inputs from a connected socket and relays them to the central broadcast hub.
 */
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final int clientId;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public ClientHandler(Socket socket, int clientId) {
        this.socket = socket;
        this.clientId = clientId;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Handshake protocol sending assigned ID
            out.println("WELCOME:" + clientId);

            // Fetch handle
            this.username = in.readLine();
            if (this.username == null || this.username.trim().isEmpty()) {
                this.username = "User#" + clientId;
            }

            // Broadcast entry notification
            String joinMsg = "\u001B[38;2;255;215;0m[SYSTEM] " + username + " [ID #" + clientId + "] synchronized to the grid.\u001B[0m";
            ChatServer.broadcast(joinMsg, clientId);

            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                if (clientMessage.equalsIgnoreCase("/exit")) {
                    break;
                }
                // Glow styled message payload format
                String formatted = String.format("\u001B[38;2;189;0;255m[#%d %s]:\u001B[0m \u001B[38;2;0;255;255m%s\u001B[0m", 
                        clientId, username, clientMessage);
                ChatServer.broadcast(formatted, clientId);
            }

        } catch (IOException e) {
            System.err.println("[HANDLER ERROR] Client #" + clientId + " communication lost.");
        } finally {
            ChatServer.removeClient(clientId);
            closeResources();
        }
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    private void closeResources() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
