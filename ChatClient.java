
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;

    private static final String RESET = "\u001B[0m";
    private static final String GLOW_GREEN = "\u001B[38;2;57;255;20m";
    private static final String GLOW_CYAN = "\u001B[38;2;0;255;255m";
    private static final String GLOW_PURPLE = "\u001B[38;2;189;0;255m";
    private static final String BOLD = "\u001B[1m";

    public static void main(String[] args) {
        printCyberHeader();

        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            String welcomeMsg = in.readLine();
            String clientId = "UNKNOWN";
            if (welcomeMsg != null && welcomeMsg.startsWith("WELCOME:")) {
                clientId = welcomeMsg.split(":")[1];
            }

            System.out.print(GLOW_CYAN + ">> DEFINE YOUR CYBER HANDLE: " + RESET);
            String handle = scanner.nextLine();
            out.println(handle);

            System.out.println(GLOW_GREEN + "[+] Authenticated as " + handle + " [UID #" + clientId + "]. Ready to broadcast!\n" + RESET);
            System.out.println(GLOW_PURPLE + "====================== LIVE STREAM START ======================" + RESET);

            Thread listenerThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    System.out.println(GLOW_PURPLE + "\n[!] Connection to stream terminated." + RESET);
                }
            });
            listenerThread.setDaemon(true);
            listenerThread.start();

            while (true) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("/exit")) {
                    out.println("/exit");
                    break;
                }
                out.println(input);
            }

            System.out.println(GLOW_CYAN + "[*] Safely exiting network... Goodbye!" + RESET);

        } catch (IOException e) {
            System.err.println("\u001B[31m[ERROR] Server connection failed at " + SERVER_HOST + ":" + SERVER_PORT + RESET);
        }
    }

    private static void printCyberHeader() {
        System.out.println(GLOW_GREEN + BOLD + "======================================================================" + RESET);
        System.out.println(GLOW_CYAN + BOLD  + "   ______   ______  ______  ______  ______   ______  __  __   ______  " + RESET);
        System.out.println(GLOW_CYAN + BOLD  + "  / __  \\\\ / __  \\\\/ __  \\\\/ ____ \\\\/ __  \\\\ / __  \\\\/ / / /  / __  \\\\ " + RESET);
        System.out.println(GLOW_PURPLE + BOLD+ " / /_/ // /_/ // /_/ // /__/ // /_/ // /_/ // /_/ /  / /_/ / " + RESET);
        System.out.println(GLOW_PURPLE + BOLD+ " \\\\____/ \\\\____//_/\\\\__/ \\\\_____//_/\\\\__/ \\\\____/ \\\\____/   \\\\____/  " + RESET);
        System.out.println(GLOW_GREEN + BOLD + "              CYBER DARK GLOW TERMINAL CHAT SYSTEM v1.0              " + RESET);
        System.out.println(GLOW_GREEN + BOLD + "======================================================================" + RESET);
    }
}

