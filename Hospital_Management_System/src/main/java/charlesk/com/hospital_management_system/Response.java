/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package charlesk.com.hospital_management_system;
import java.io.Serializable;

/**
 *
 * @author charles
 */

public class Response implements Serializable {

    private static final long serialVersionUID = 1L;

    private long requestId;
    private int statusCode;
    private String message;
    private Object data;

    public Response(long requestId, int statusCode,
                    String message, Object data) {

        this.requestId = requestId;
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public long getRequestId() {
        return requestId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public boolean isSuccessful() {
        return statusCode == 200;
    }
}