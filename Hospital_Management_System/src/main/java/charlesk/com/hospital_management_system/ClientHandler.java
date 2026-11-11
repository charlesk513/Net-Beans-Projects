
package charlesk.com.hospital_management_system;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/** Handles communication with one connected client application. */
public class ClientHandler implements Runnable {

    private final Socket socket;
    private final RequestRouter router;

    public ClientHandler(Socket socket) {
        this(socket, new RequestRouter());
    }

    public ClientHandler(Socket socket, RequestRouter router) {
        this.socket = socket;
        this.router = router;
    }

    @Override
    public void run() {

        try (Socket client = socket;
             ObjectOutputStream output =
                     new ObjectOutputStream(client.getOutputStream())) {

            // Send the object stream header first.
            output.flush();

            // Receive the client's object stream.
            try (ObjectInputStream input =
                         new ObjectInputStream(client.getInputStream())) {

                System.out.println(
                        "Client connected: " +
                        client.getRemoteSocketAddress()
                );

                while (true) {

                    Object received;

                    try {
                        received = input.readObject();

                    } catch (EOFException ex) {
                        // Client has closed its connection.
                        break;
                    }

                    // Process the received request.
                    Response response = processRequest(received);

                    // Send the response back to the client.
                    synchronized (output) {
                        output.writeObject(response);
                        output.flush();

                        // Prevent previously serialized objects
                        // from being reused from the stream cache.
                        output.reset();
                        output.flush();
                    }
                }
            }

        } catch (IOException | ClassNotFoundException ex) {

            System.err.println(
                    "Client handler ended: " + ex.getMessage()
            );

        } finally {

            System.out.println(
                    "Client disconnected: " +
                    socket.getRemoteSocketAddress()
            );
        }
    }

    /**
     * Validates and forwards an incoming Request.
     */
    private Response processRequest(Object received) {

        if (!(received instanceof Request)) {
            return new Response(
                    -1L,
                    400,
                    "Invalid message. Expected a Request.",
                    null
            );
        }

        Request request = (Request) received;

        return router.route(request);
    }
}
