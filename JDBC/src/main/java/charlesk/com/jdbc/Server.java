package charlesk.com.jdbc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.ResultSet;

/**
 *
 * @author HP
 */
public class Server extends javax.swing.JFrame {
    ObjectOutputStream serverObjectStreamWriter;
    ObjectInputStream serverObjectStreamReader;
     
    ServerSocket serverSock ;
    Socket clientSocket;
    SerializableClass serialObj;
    
    public Server() {
        initComponents();
        serverObjectStreamWriter = null;
        serverObjectStreamReader = null;
        serverSock = null;
        clientSocket = null;
        serialObj = null;     
    }
    
    public void go() {
        try{
	    serverSock = new ServerSocket(50000);
	    
	    serialObj= new SerializableClass();
            clientSocket = serverSock.accept();

            serverObjectStreamWriter = new ObjectOutputStream(clientSocket.getOutputStream());
            serverObjectStreamWriter.flush();

            serverObjectStreamReader = new ObjectInputStream(clientSocket.getInputStream());

            Thread t = new Thread(new ServerRunnable());

            t.start();
            System.out.println("got a connection");
            
	    }catch(Exception ex){
             ex.printStackTrace();
	    }
    }
    
    public class ServerRunnable implements Runnable{

        
public void run() {
    try {
        while (true) {
            SerializableClass request =
                (SerializableClass) serverObjectStreamReader.readObject();

            if (request == null) {
                break;
            }

            // Use the object received from the client
            serialObj = request;

            txtRegNo.setText(request.regno);
            txtSurName.setText(request.surname);

            if ("find".equals(request.status)) {
                find();
            } else if ("save".equals(request.status)) {
                save();
            } else {
                System.out.println("Unknown request: " + request.status);
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
        lblRegNo = new javax.swing.JLabel();
        txtRegNo = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Server");

        lblSurName.setText("SurName");

        lblRegNo.setText("RegNo");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSurName)
                    .addComponent(lblRegNo))
                .addGap(63, 63, 63)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtRegNo, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                    .addComponent(txtSurName))
                .addContainerGap(105, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRegNo)
                    .addComponent(txtRegNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSurName)
                    .addComponent(txtSurName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(156, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        Server s = new Server();
        s.setVisible(true);
        s.setLocationRelativeTo(null);
        s.go();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblRegNo;
    private javax.swing.JLabel lblSurName;
    private javax.swing.JTextField txtRegNo;
    private javax.swing.JTextField txtSurName;
    // End of variables declaration//GEN-END:variables

   
    public void clear(){
        txtRegNo.setText("");
        txtSurName.setText("");
    }
    public void save(){
        serialObj.regno   = txtRegNo.getText();
        serialObj.surname = txtSurName.getText();
        String regno   = txtRegNo.getText();
        String surname = txtSurName.getText();
        
        if(surname.isEmpty()){
        JOptionPane.showMessageDialog(this, "Enter Surname");
        txtSurName.requestFocus();
        return;
        }
        else if(regno.isEmpty()){
            JOptionPane.showMessageDialog(this, "Enter Reg number");
            txtRegNo.requestFocus();
            return;
        }
        
        String sql = "INSERT INTO store(surname, regno) VALUES (?, ?);";
        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)
        ){
            pst.setString(1, surname);
            pst.setString(2, regno);
            
            int result = pst.executeUpdate();
            if(result > 0){
                JOptionPane.showMessageDialog(this, "Credentials saved successfully");
                clear();
            }else
            JOptionPane.showMessageDialog(this, "Something wrong happened");

        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
    }
    
    
public void find() {
    String regno = serialObj.regno;

    String sql = "SELECT surname, regno FROM store WHERE regno = ?";
    JOptionPane.showMessageDialog(this, "Find method");
    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement pst = con.prepareStatement(sql)) {

        pst.setString(1, regno);

        try (ResultSet result = pst.executeQuery()) {
            if (result.next()) {
                serialObj.regno = result.getString("regno");
                serialObj.surname = result.getString("surname");
                serialObj.status = "findResult";
            } else {
                serialObj.regno = regno;
                serialObj.surname = "";
                serialObj.status = "notFound";
            }
        }

        serverObjectStreamWriter.writeObject(serialObj);
        serverObjectStreamWriter.flush();

    } catch (SQLException | IOException e) {
        e.printStackTrace();
    }
}


}
