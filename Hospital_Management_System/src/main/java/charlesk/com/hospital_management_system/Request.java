package charlesk.com.hospital_management_system;

import java.io.Serializable;

public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    private long requestId;
    private String sourceForm;
    private String entity;
    private String operation;
    private Object payload;

    public Request(long requestId, String sourceForm,
                   String entity, String operation, Object payload) {

        this.requestId = requestId;
        this.sourceForm = sourceForm;
        this.entity = entity;
        this.operation = operation;
        this.payload = payload;
    }

    public long getRequestId() {
        return requestId;
    }

    public String getSourceForm() {
        return sourceForm;
    }

    public String getEntity() {
        return entity;
    }

    public String getOperation() {
        return operation;
    }

    public Object getPayload() {
        return payload;
    }
}