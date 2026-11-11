package charlesk.com.hospital_management_system;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.SwingUtilities;

/** Manages requests and responses for all GUI frames. */
public class RequestManager {

    private static RequestManager instance;

    private final ClientConnector connector;
    private final Map<Long, ActionListener> pendingRequests;

    private long nextRequestId = 1000;

    private RequestManager() {

        connector = new ClientConnector();
        pendingRequests = new HashMap<>();

        // Register the central response handler.
        connector.setResponseHandler(this::handleResponse);
    }

    /** Returns the single shared RequestManager instance. */
    public static synchronized RequestManager getInstance() {

        if (instance == null) {
            instance = new RequestManager();
        }

        return instance;
    }

    /** Generates a unique request ID. */
    private synchronized long generateRequestId() {

        nextRequestId++;

        return nextRequestId;
    }

    /** Establishes the connection to the local hospital server. */
    public void connect() throws IOException {
        connector.connect();
    }

    /** Checks whether the client is connected to the server. */
    public boolean isConnected() {

        return connector.isConnected();
    }

    /**
     * Sends a request and associates it with the GUI callback.
     *
     * The callback receives the corresponding Response through
     * an ActionEvent whose source is the Response object.
     */
    public long sendRequest(
        String sourceForm,
        String entity,
        String operation,
        DataDTO payload,
        ActionListener callback) {

        long requestId = generateRequestId();

        Request request = new Request(
                requestId,
                sourceForm,
                entity,
                operation,
                payload
        );

        // Register callback before sending the request.
        if (callback != null) {
            synchronized (pendingRequests) {
                pendingRequests.put(requestId, callback);
            }
        }

        try {

            connector.send(request);

        } catch (IOException ex) {

            // Remove the callback because the request was not sent.
            synchronized (pendingRequests) {
                pendingRequests.remove(requestId);
            }

            Response response = new Response(requestId, 500, "Could not send request: " + ex.getMessage(), null);

            notifyCallback(callback, response);
        }

        return requestId;
    }

    /**
     * Receives responses from ClientConnector and matches
     * them to their original requests.
     */
    private void handleResponse(Response response) {

        ActionListener callback;

        synchronized (pendingRequests) {
            callback = pendingRequests.remove(
                    response.getRequestId()
            );
        }

        if (callback != null) {
            notifyCallback(callback, response);
        }
    }

    /** Delivers a response to its GUI callback on the Swing EDT. */
    private void notifyCallback(
            ActionListener callback,
            Response response) {

        if (callback == null) {
            return;
        }

        SwingUtilities.invokeLater(() -> {

            callback.actionPerformed(
                    new ActionEvent(response, ActionEvent.ACTION_PERFORMED, "SERVER_RESPONSE"
                    )
            );
        });
    }

    /** Closes the connection and clears pending callbacks. */
    public void close() {

        connector.close();

        synchronized (pendingRequests) {
            pendingRequests.clear();
        }
    }
}
