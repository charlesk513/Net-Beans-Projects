package charlesk.com.hospital_management_system;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** TCP server entry point. Start this before starting the Swing client. */
public final class HospitalServer implements AutoCloseable {
    public static final int DEFAULT_PORT = 50000;

    private final int port;
    private final ExecutorService clientPool = Executors.newCachedThreadPool();
    private volatile boolean running;
    private ServerSocket serverSocket;

    public HospitalServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port, 50, InetAddress.getLoopbackAddress());
        running = true;
        System.out.println("Hospital server listening on port " + port);
        while (running) {
            try {
                Socket client = serverSocket.accept();
                System.out.println("Accepted client: " + client.getRemoteSocketAddress());
                clientPool.execute(new ClientHandler(client, new RequestRouter()));
            } catch (IOException ex) {
                if (running) System.err.println("Server accept failed: " + ex.getMessage());
            }
        }
    }

    @Override
    public void close() {
        running = false;
        if (serverSocket != null) {
            try { serverSocket.close(); } catch (IOException ignored) { }
        }
        clientPool.shutdownNow();
        System.out.println("Hospital server stopped.");
    }

    public static void main(String[] args) {
        int port = Integer.getInteger("hospital.server.port", DEFAULT_PORT);
        HospitalServer server = new HospitalServer(port);
        Runtime.getRuntime().addShutdownHook(new Thread(server::close));
        try {
            server.start();
        } catch (IOException ex) {
            System.err.println("Unable to start hospital server: " + ex.getMessage());
        } finally {
            server.close();
        }
    }
}
