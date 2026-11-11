package charlesk.com.hospital_management_system;

public class RequestRouter {

    private final HospitalService hospitalService;

    public RequestRouter() {
        this(new HospitalService());
    }

    public RequestRouter(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    public Response route(Request request) {
        if (request == null) {
            return new Response(-1L, 400, "Request was null.", null);
        }

        long requestId = request.getRequestId();

        try {
            // Request carries a generic Object payload. All GUI frames should
            // send DataDTO, so convert it back to DataDTO here.
            Object payload = request.getPayload();
            if (payload != null && !(payload instanceof DataDTO)) {
                return new Response(requestId, 400,
                        "Invalid payload. Expected DataDTO but received "
                                + payload.getClass().getName(), null);
            }

            DataDTO dto = payload == null ? new DataDTO() : (DataDTO) payload;

            // Entity examples: PATIENTS, DOCTORS, APPOINTMENTS, LOGIN.
            // Operation examples: SAVE, UPDATE, FIND, FIRST, LAST, PREVIOUS.
            Object result = hospitalService.execute(
                request.getSourceForm(),
                request.getEntity(),
                request.getOperation(),
                dto
        );

            return new Response(requestId, 200,
                    "Request completed successfully.", result);

        } catch (IllegalArgumentException e) {
            return new Response(requestId, 400, e.getMessage(), null);

        } catch (UnsupportedOperationException e) {
            // Expected until you implement the SQL for that service method.
            return new Response(requestId, 501, e.getMessage(), null);

        } catch (Exception e) {
            // In production, log the full exception on the server.
            return new Response(requestId, 500,
                    "Server could not process the request: " + e.getMessage(), null);
        }
    }
}
