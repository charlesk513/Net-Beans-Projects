package charlesk.com.jdbc;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author charles
 */
public class GuiRow extends javax.swing.JFrame implements ActionListener{
    
    JLabel lbl_name;
    JLabel lbl_reg = new JLabel();
    JTextField txt_name;
    JTextField txt_reg;
    JButton btn_save;
    JButton btn_reset;
    
    public GuiRow(){
        setTitle("Practice Gui row");
        JPanel panel = new JPanel(new GridLayout(3,2,40,35));
        setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        
        lbl_name = new JLabel();
        txt_name = new JTextField();
        
        lbl_reg = new JLabel();
        txt_reg = new JTextField();
        
        btn_save = new JButton();
        btn_reset = new JButton();
        
        lbl_name.setText("Name");
        lbl_reg.setText("Reg");
        
        btn_save.setText("Save");
        btn_reset.setText("Reset");
        
        panel.add(lbl_name);
        panel.add(txt_name);
        
        panel.add(lbl_reg);
        panel.add(txt_reg);
        
        panel.add(btn_reset);
        panel.add(btn_save);
        add(panel);
        
        setLocationRelativeTo(null);
        setSize(400, 200);
        
        btn_save.addActionListener(this);
        btn_reset.addActionListener(this);
    }
    
    public static void main(String[] args){
        
            GuiRow gui = new GuiRow();
            gui.setVisible(true); 
 
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String caption = e.getActionCommand();
        if(caption.equals("Save")){
            JOptionPane.showMessageDialog(null, "Saved successfully");
        } else if (caption.equals("Reset")){
            System.exit(0);
        }
    }
}
