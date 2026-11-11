# Hospital Management System — Phase 1 (Client/Server)

## What changed

This phase introduces a TCP client/server boundary and migrates authentication to the server.

New classes:
- `HospitalServer`: server entry point; listens on TCP port 5050.
- `ClientRuntime`: one connection manager for the running Swing client.
- `AppLauncher`: connects before displaying the Login frame.

Updated classes:
- `ClientConnector`: owns the client's TCP socket, object streams, and response-listener thread.
- `ClientHandler`: one server-side handler per connected client.
- `HospitalService`: implements LOGIN, registration, username check, and password update.
- `Login`, `SignIn`, `PasswordReset`: send requests through `RequestManager`; they no longer call JDBC.

The existing SQL text and parameter order for the four authentication statements are retained.

## NetBeans run order

1. Ensure MySQL is running and the existing `hospital_management` database and `login` table are available.
2. Ensure the MySQL JDBC driver is on the server application's classpath.
3. Run `charlesk.com.hospital_management_system.HospitalServer` first.
   - It listens on `127.0.0.1:5050`.
   - Keep this process running while clients use the application.
4. Run `charlesk.com.hospital_management_system.Login`.
   - `AppLauncher` connects to the server first.
   - The Login frame appears only after a successful connection.
5. Open multiple client application instances if required. Each process creates its own TCP connection. Frames inside one process share that process's connection.

## Optional connection settings

The server port can be changed with the JVM property:
`-Dhospital.server.port=5050`

The client host can be changed with:
`-Dhospital.server.host=127.0.0.1`

The client port uses the same `hospital.server.port` property.

## Important deployment note

The uploaded source archive did not include a NetBeans project configuration or the external libraries used by the existing forms. The full application therefore needs its original dependencies configured in NetBeans (including the MySQL JDBC driver, JCalendar, and iText, where used).

For deployment, create separate server and client build artifacts. Only the server artifact should contain `DatabaseConnection` and database configuration. The database credential is currently hard-coded in the original source; rotate it and move it to server-only environment/configuration before deployment. Do not distribute the server artifact or its configuration to client machines.

## Current authentication limitations

This migration deliberately preserves the current database schema and SQL behavior. Passwords are still stored and compared in the same form as the existing application. This is not suitable for production: the next security phase should migrate existing passwords to salted password hashes and update authentication accordingly.

## Validation performed

- Core networking, DTO, routing, service, and database-connection Java classes compile.
- The three migrated authentication frames and launcher compile against the networking classes.
- A local TCP smoke test connected a client to the server, sent a serialized request, and received the matching serialized response.

The complete GUI source set was not compiled in this environment because the original external JCalendar and iText dependencies were not included in the uploaded archive.
