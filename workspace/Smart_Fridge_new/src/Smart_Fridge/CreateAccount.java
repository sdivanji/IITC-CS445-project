package Smart_Fridge;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class CreateAccount implements ActionListener {

    JFrame          frame;
    JPanel          panel;
    JTextField      username;
    JTextField      email;
    JPasswordField  password;
    JPasswordField  confirmPassword;
    JLabel          warningLabel;

    
//Constructor
    public CreateAccount() {
        panel = new JPanel();

        frame = new JFrame("Create a new account");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(BorderLayout.CENTER, panel);

        JLabel userLabel = new JLabel("Username:");
        JLabel emailLabel = new JLabel ("E-mail id");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        username = new JTextField("", 15);
        email = new JTextField("", 20);
        password = new JPasswordField("", 15);
        password.setEchoChar('*');
        confirmPassword = new JPasswordField("", 15);
        confirmPassword.setEchoChar('*');
        //pattern to check valid email address
        final String EMAIL_PATTERN = 
        		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
        		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
         

        GridBagConstraints right = new GridBagConstraints();
        right.anchor = GridBagConstraints.WEST;
        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.EAST;

        right.weightx = (int) 2;
        right.fill = GridBagConstraints.REMAINDER;
        right.gridwidth = GridBagConstraints.REMAINDER;
        // actual GUI
        //add components to the window
        panel.add(userLabel, left);
        panel.add(username, right);
        panel.add(emailLabel,left);
      
        panel.add(email,right);
        
        panel.add(passwordLabel, left);
        panel.add(password, right);
        panel.add(confirmPasswordLabel, left);
        panel.add(confirmPassword, right);

        JButton createAccount = new JButton("Create this account");
        frame.getContentPane().add(BorderLayout.SOUTH, createAccount);
        createAccount.addActionListener(this);

        warningLabel = new JLabel();
        frame.getContentPane().add(BorderLayout.NORTH, warningLabel);

        frame.setSize(300, 250);
        frame.setVisible(true);
    }

    //Action listener
    public void actionPerformed(ActionEvent event) {
    
    	
    	
    	
       	EmailValidator valid_email = new EmailValidator();
    	boolean eflag= valid_email.validate(email.getText());
    	
    	if(!eflag) {
    		warningLabel.setText("Enter valid email");
    	}
    	
		   
		  if (eflag) {
    	if (!(Arrays.equals(password.getPassword(),
                confirmPassword.getPassword()))) {
            warningLabel
                    .setText("Your passwords do not match! Please try again.");
        } else if (password.getPassword().length < 1) {
            warningLabel
                    .setText("That password is not long enough! Please try again!");
        } else {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(
                        "nuserInfo.txt",true));
                writer.write(username.getText() + "/" + new String(password.getPassword())+ "/" + new String(email.getText()) + "\n");
                Message_pop.infoBox("Account created succesfully", "Info");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    	}  
			  
        }
    


public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
            try {
                UIManager.setLookAndFeel(UIManager
                        .getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            CreateAccount window = new CreateAccount();
        }
    });
}
}