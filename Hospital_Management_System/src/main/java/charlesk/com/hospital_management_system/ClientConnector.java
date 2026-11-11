
package charlesk.com.hospital_management_system;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.function.Consumer;

/** Owns the TCP connection used by one running GUI application. */
public class ClientConnector implements AutoCloseable {

    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 50000;

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private volatile Consumer<Response> responseHandler;
    private volatile boolean running;

    /**
     * Connect using the default hospital server address.
     */
    public synchronized void connect() throws IOException {
        connect(DEFAULT_HOST, DEFAULT_PORT);
    }

    /**
     * Connect to the specified hospital server.
     */
    public synchronized void connect(String host, int port)
            throws IOException {

        if (isConnected()) {
            return;
        }

        Socket newSocket = new Socket();

        try {
            // Establish TCP connection.
            newSocket.connect(
                    new InetSocketAddress(host, port), 5000
            );

            // Create output stream first to exchange stream headers.
            ObjectOutputStream newOutput =
                    new ObjectOutputStream(newSocket.getOutputStream());

            newOutput.flush();

            // Create input stream after output stream.
            ObjectInputStream newInput =
                    new ObjectInputStream(newSocket.getInputStream());

            socket = newSocket;
            output = newOutput;
            input = newInput;

            running = true;

            // Start listening for server responses.
            Thread listener = new Thread(
                    this::listenForResponses,
                    "Hospital-Client-Response-Listener"
            );

            listener.setDaemon(true);
            listener.start();

            System.out.println(
                    "Connected to hospital server at "
                    + host + ":" + port
            );

        } catch (IOException ex) {

            try {
                newSocket.close();
            } catch (IOException ignored) {
            }

            throw ex;
        }
    }

    /**
     * Register the method that will handle server responses.
     */
    public void setResponseHandler(Consumer<Response> handler) {
        responseHandler = handler;
    }

    /**
     * Send a Request object to the server.
     */
    public synchronized void send(Request request)
            throws IOException {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Request cannot be null."
            );
        }

        if (!isConnected() || output == null) {
            throw new IOException(
                    "Not connected to the hospital server."
            );
        }

        output.writeObject(request);
        output.flush();

        // Clear the serialization cache.
        output.reset();
        output.flush();
    }

    /**
     * Continuously listen for responses from the server.
     */
    private void listenForResponses() {

        ObjectInputStream responseInput = input;

        while (running) {

            try {
                Object received = responseInput.readObject();

                if (received instanceof Response) {

                    Response response = (Response) received;

                    Consumer<Response> handler = responseHandler;

                    if (handler != null) {
                        try {
                            handler.accept(response);

                        } catch (RuntimeException ex) {
                            System.err.println(
                                    "Error processing server response: "
                                    + ex.getMessage()
                            );
                        }
                    }
                }
                else {

                    System.err.println(
                            "Ignoring unexpected server message."
                    );
                }

            } catch (EOFException ex) {

                System.out.println(
                        "Server closed the connection."
                );

                close();
                break;

            } catch (IOException | ClassNotFoundException ex) {

                if (running) {
                    System.err.println(
                            "Server connection ended: "
                            + ex.getMessage()
                    );
                }

                close();
                break;
            }
        }
    }

    /**
     * Check whether the connection is active.
     */
    public synchronized boolean isConnected() {

        return running
                && socket != null
                && socket.isConnected()
                && !socket.isClosed();
    }

    /**
     * Close the connection and release resources.
     */
    @Override
    public synchronized void close() {

        running = false;

        Socket current = socket;

        socket = null;
        input = null;
        output = null;

        if (current != null) {
            try {
                current.close();
            } catch (IOException ignored) {
            }
        }

        System.out.println("Hospital server connection closed.");
    }
}
