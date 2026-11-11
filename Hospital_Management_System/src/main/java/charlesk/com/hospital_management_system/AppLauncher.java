
package charlesk.com.hospital_management_system;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public final class AppLauncher {

    private AppLauncher() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppLauncher::startClient);
    }

    public static void startClient() {

        SwingWorker<Void, Void> worker = new SwingWorker<>() {

            @Override
            protected Void doInBackground() throws Exception {
                RequestManager.getInstance().connect();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();

                    Runtime.getRuntime().addShutdownHook(
                        new Thread(() ->
                            RequestManager.getInstance().close()
                        )
                    );

                    new Login().setVisible(true);

                } catch (Exception ex) {

                    RequestManager.getInstance().close();

                    JOptionPane.showMessageDialog(
                        null,
                        "Could not connect to the server.\n"
                        + "Make sure HospitalServer is running.\n\n"
                        + ex.getMessage(),
                        "Connection Error",
                        JOptionPane.ERROR_MESSAGE
                    );

                    System.exit(1);
                }
            }
        };

        worker.execute();
    }
}
