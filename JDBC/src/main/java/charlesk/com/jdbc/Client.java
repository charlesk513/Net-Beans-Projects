package charlesk.com.jdbc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class Client extends javax.swing.JFrame {
    ObjectOutputStream clientObjectStreamWriter;

    ObjectInputStream clientObjectStreamReader;

    Socket sock;
    SerializableClass serialObj;

    public Client() {
        initComponents();
        clientObjectStreamWriter = null;
        clientObjectStreamReader = null;
        sock = null;
        serialObj= new SerializableClass();
    }
    public void go() {
        try {
            sock = new Socket("127.0.0.1", 50000);

            clientObjectStreamWriter =
                    new ObjectOutputStream(sock.getOutputStream());
            clientObjectStreamWriter.flush();

            clientObjectStreamReader =
                    new ObjectInputStream(sock.getInputStream());

            System.out.println("Networking established");

            Thread t = new Thread(new ClientRunnable());
            t.start();

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this, "Could not connect to the server."
            );
        }
    }

    public class ClientRunnable implements Runnable{
    
    public void run() {
        try {
            while (true) {
                SerializableClass response = (SerializableClass) clientObjectStreamReader.readObject();
                if (response == null) {
                    break;
                }

                if ("findResult".equals(response.status)) {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        txtRegNo.setText(response.regno);
                        txtSurName.setText(response.surname);
                    });

                } else if ("notFound".equals(response.status)) {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        txtSurName.setText("");
                        JOptionPane.showMessageDialog(Client.this,"No record found for registration number: " + response.regno);
                    });
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblSurName = new javax.swing.JLabel();
        txtSurName = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        lblRegNo = new javax.swing.JLabel();
        txtRegNo = new javax.swing.JTextField();
        btnFind = new javax.swing.JButton();
        btn_clear = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Client");

        lblSurName.setText("SurName");

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        lblRegNo.setText("RegNo");

        btnFind.setText("Find");
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });

        btn_clear.setText("Clear");
        btn_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(btnSave)
                        .addGap(35, 35, 35)
                        .addComponent(btnFind)
                        .addGap(35, 35, 35)
                        .addComponent(btn_clear))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSurName)
                            .addComponent(lblRegNo))
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSurName, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                            .addComponent(txtRegNo))))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRegNo)
                    .addComponent(txtRegNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSurName)
                    .addComponent(txtSurName, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(91, 91, 91)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnFind)
                    .addComponent(btn_clear))
                .addGap(58, 58, 58))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        serialObj.regno = txtRegNo.getText(); 
        serialObj.surname = txtSurName.getText();
        serialObj.status = "save";
        if(serialObj.surname.isEmpty()){
        JOptionPane.showMessageDialog(this, "Enter Surname");
        txtSurName.requestFocus();
        return;
        }
        else if(serialObj.regno.isEmpty()){
            JOptionPane.showMessageDialog(this, "Enter Reg number");
            txtRegNo.requestFocus();
            return;
        }
        try {            
            clientObjectStreamWriter.writeObject(serialObj);
            clientObjectStreamWriter.flush(); // not to corrupt the stream
            System.out.println("sent");
         } catch (IOException ex) {
             ex.printStackTrace();
         }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed
        String regno = txtRegNo.getText().trim();

        if (regno.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Please fill in your registration number"
            );
            return;
        }

        // Clear the previous result from the client window
        txtSurName.setText("");

        serialObj.regno = regno;
        serialObj.surname = "";
        serialObj.status = "find";

        try {
            clientObjectStreamWriter.writeObject(serialObj);
            clientObjectStreamWriter.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }   
    }//GEN-LAST:event_btnFindActionPerformed

    private void btn_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearActionPerformed
        clear();
    }//GEN-LAST:event_btn_clearActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        Client c = new Client();
        c.setLocationRelativeTo(null);
        c.go();

    }
    
    public void clear(){
        txtRegNo.setText("");
        txtSurName.setText("");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFind;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btn_clear;
    private javax.swing.JLabel lblRegNo;
    private javax.swing.JLabel lblSurName;
    private javax.swing.JTextField txtRegNo;
    private javax.swing.JTextField txtSurName;
    // End of variables declaration//GEN-END:variables
}
